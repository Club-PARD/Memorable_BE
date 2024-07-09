package com.study.memorable.TestSheet.service;

import com.study.memorable.TestSheet.dto.TestSheetDetailReadDTO;
import com.study.memorable.TestSheet.dto.TestSheetReadDTO;
import com.study.memorable.TestSheet.dto.TestSheetSimpleReadDTO;
import com.study.memorable.TestSheet.dto.TestSheetUpdateDTO;
import com.study.memorable.TestSheet.entity.TestSheet;
import com.study.memorable.TestSheet.repo.TestSheetRepo;
import com.study.memorable.WorkSheet.entity.WorkSheet;
import com.study.memorable.WorkSheet.repo.WorkSheetRepo;
import com.study.memorable.Questions.dto.QuestionsCreateDTO;
import com.study.memorable.Questions.dto.QuestionsReadDTO;
import com.study.memorable.Questions.entity.Questions;
import com.study.memorable.Questions.Repo.QuestionsRepo;
import com.study.memorable.OpenAI.controller.OpenAIController;
import com.study.memorable.File.entity.File;
import com.study.memorable.File.repo.FileRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class TestSheetService {
    private final TestSheetRepo testSheetRepo;
    private final WorkSheetRepo workSheetRepo;
    private final QuestionsRepo questionsRepo;
    private final OpenAIController openAIController;

    int len = 3;

    @Transactional
    public TestSheetReadDTO createTestSheet(Long worksheetId) {
        WorkSheet worksheet = workSheetRepo.findById(worksheetId)
                .orElseThrow(() -> new RuntimeException("Worksheet not found"));

        File file = worksheet.getFile();
        List<String> keywords = openAIController.extractKeywordsFromContent(file.getContent());

        List<String> oddKeywords = IntStream.range(0, keywords.size())
                .filter(i -> i % 2 == 1)
                .mapToObj(keywords::get)
                .collect(Collectors.toList());

        List<String> evenKeywords = IntStream.range(0, keywords.size())
                .filter(i -> i % 2 == 0)
                .mapToObj(keywords::get)
                .collect(Collectors.toList());

        Map<String, List<Questions>> firstQuestions = generateAndSaveQuestions(oddKeywords, file);
        Map<String, List<Questions>> secondQuestions = generateAndSaveQuestions(evenKeywords, file);

        log.info("First set of questions: {}", firstQuestions);
        log.info("Second set of questions: {}", secondQuestions);

        List<QuestionsReadDTO> questions1 = firstQuestions.get("questions").stream()
                .map(this::toQuestionsReadDTO)
                .collect(Collectors.toList());

        List<QuestionsReadDTO> questions2 = secondQuestions.get("questions").stream()
                .map(this::toQuestionsReadDTO)
                .collect(Collectors.toList());

        log.info("Converted first set of questions to DTO: {}", questions1);
        log.info("Converted second set of questions to DTO: {}", questions2);

        TestSheet testSheet = TestSheet.builder()
                .file(file)
                .bookmark(false)
                .isCompleteAllBlanks(Arrays.asList(false, false))
                .created_date(LocalDateTime.now())
                .score(Arrays.asList(0, 0))
                .isCorrect(new ArrayList<>(Collections.nCopies((2*len), false)))
//                .isCorrect(new ArrayList<>(Collections.nCopies(40, false)))
                .build();
        testSheetRepo.save(testSheet);

        return TestSheetReadDTO.toDTO(testSheet, questions1, questions2);
    }

    @Transactional
    public TestSheetUpdateDTO updateUserAnswers(Long testsheetId, TestSheetUpdateDTO userAnswersDTO) {
        TestSheet testSheet = testSheetRepo.findById(testsheetId)
                .orElseThrow(() -> new RuntimeException("TestSheet not found"));

        // Update isReExtracted
        testSheet.setReExtracted(userAnswersDTO.isReExtracted());

        // Update isCompleteAllBlanks
        List<Boolean> currentIsCompleteAllBlanks = testSheet.getIsCompleteAllBlanks();
        List<Boolean> requestIsCompleteAllBlanks = userAnswersDTO.getIsCompleteAllBlanks();

        boolean isUserAnswers1Complete = requestIsCompleteAllBlanks.get(0) && !currentIsCompleteAllBlanks.get(0);
        boolean isUserAnswers2Complete = requestIsCompleteAllBlanks.get(1) && !currentIsCompleteAllBlanks.get(1);

        boolean isUserAnswers1Reverted = !requestIsCompleteAllBlanks.get(0) && currentIsCompleteAllBlanks.get(0);
        boolean isUserAnswers2Reverted = !requestIsCompleteAllBlanks.get(1) && currentIsCompleteAllBlanks.get(1);

        if (isUserAnswers1Complete) {
            processUserAnswers(testSheet, userAnswersDTO.getUserAnswers1(), 0);
        }

        if (isUserAnswers2Complete) {
            processUserAnswers(testSheet, userAnswersDTO.getUserAnswers2(), len);
//            processUserAnswers(testSheet, userAnswersDTO.getUserAnswers2(), 20);
        }

        if (isUserAnswers1Reverted || isUserAnswers2Reverted) {
            revertUserAnswers(testSheet, requestIsCompleteAllBlanks);
        }

        testSheet.setIsCompleteAllBlanks(requestIsCompleteAllBlanks);
        testSheetRepo.save(testSheet);

        return TestSheetUpdateDTO.fromEntity(testSheet);
    }

    private void processUserAnswers(TestSheet testSheet, List<String> userAnswers, int startIdx) {
        File file = testSheet.getFile();
        List<Questions> questions = questionsRepo.findByFile(file);

        List<String> allQuestions = new ArrayList<>();
        List<String> allAnswers = new ArrayList<>();
        List<String> allUserAnswers = new ArrayList<>();

        List<Boolean> isCorrectList = testSheet.getIsCorrect();
        List<Integer> scoreList = testSheet.getScore();

        for (int i = 0; i < userAnswers.size(); i++) {
            Questions question = questions.get(i + startIdx);
            question.setUser_answers(userAnswers.get(i));
            questionsRepo.save(question);
            allQuestions.add(question.getQuestions());
            allAnswers.add(question.getAnswers());
            allUserAnswers.add(question.getUser_answers());
        }

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("content", file.getContent());
        requestBody.put("questions", allQuestions);
        requestBody.put("answers", allAnswers);
        requestBody.put("userAnswers", allUserAnswers);

        // Call the scoreAnswers API
        Map<String, Object> scoringResult = openAIController.scoreAnswers(requestBody);

        int scoreIndex = startIdx == 0 ? 0 : 1;
        scoreList.set(scoreIndex, (Integer) scoringResult.get("score"));

        List<Boolean> tempIsCorrect = (List<Boolean>) scoringResult.get("isCorrect");
        for (int i = 0; i < tempIsCorrect.size(); i++) {
            isCorrectList.set(i + startIdx, tempIsCorrect.get(i));
        }

        testSheet.setScore(scoreList);
        testSheet.setIsCorrect(isCorrectList);
    }

    private void revertUserAnswers(TestSheet testSheet, List<Boolean> requestIsCompleteAllBlanks) {
        File file = testSheet.getFile();
        List<Questions> questions = questionsRepo.findByFile(file);

//        for (int i = 0; i < 20; i++) {
        for (int i = 0; i < len; i++) {
            Questions question1 = questions.get(i);
            if (!requestIsCompleteAllBlanks.get(0)) {
                question1.setUser_answers("");
            }
            questionsRepo.save(question1);

//            Questions question2 = questions.get(i + 20);
            Questions question2 = questions.get(i + len);
            if (!requestIsCompleteAllBlanks.get(1)) {
                question2.setUser_answers("");
            }
            questionsRepo.save(question2);
        }
    }


    private Map<String, List<Questions>> generateAndSaveQuestions(List<String> keywords, File file) {
        Map<String, List<String>> qaMap = processKeywordsWithService(keywords, file.getContent());
        return saveQuestions(qaMap, file);
    }

    private Map<String, List<Questions>> saveQuestions(Map<String, List<String>> qaMap, File file) {
        List<String> questions = qaMap.get("questions");
        List<String> answers = qaMap.get("answers");

        List<Questions> savedQuestions = new ArrayList<>();

        for (int i = 0; i < questions.size(); i++) {
            if (!questions.get(i).isEmpty() && !answers.get(i).isEmpty()) {
                QuestionsCreateDTO dto = QuestionsCreateDTO.builder()
                        .questions(questions.get(i))
                        .answers(answers.get(i))
                        .user_answers("")
                        .fileId(file.getId())
                        .build();
                Questions questionEntity = Questions.toEntity(dto, file);
                questionsRepo.save(questionEntity);
                savedQuestions.add(questionEntity);
            }
        }

        Map<String, List<Questions>> result = new HashMap<>();
        result.put("questions", savedQuestions);
        return result;
    }

    @Transactional(readOnly = true)
    public List<TestSheetSimpleReadDTO> getTestSheetsByUserId(String userId) {
        return testSheetRepo.findByFileUserId(userId).stream()
                .map(TestSheetSimpleReadDTO::toSimpleDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TestSheetDetailReadDTO getTestSheetById(Long testsheetId) {
        TestSheet testSheet = testSheetRepo.findById(testsheetId)
                .orElseThrow(() -> new RuntimeException("TestSheet not found"));

        File file = testSheet.getFile();
        List<Questions> questions = questionsRepo.findByFile(file);

//        List<QuestionsReadDTO> questions1 = questions.stream()
//                .limit(20)
//                .map(this::toQuestionsReadDTO)
//                .collect(Collectors.toList());
//
//        List<QuestionsReadDTO> questions2 = questions.stream()
//                .skip(20)
//                .limit(20)
//                .map(this::toQuestionsReadDTO)
//                .collect(Collectors.toList());
        List<QuestionsReadDTO> questions1 = questions.stream()
                .limit(len)
                .map(this::toQuestionsReadDTO)
                .collect(Collectors.toList());

        List<QuestionsReadDTO> questions2 = questions.stream()
                .skip(len)
                .limit(len)
                .map(this::toQuestionsReadDTO)
                .collect(Collectors.toList());

        return TestSheetDetailReadDTO.toDTO(testSheet, questions1, questions2);
    }

    private QuestionsReadDTO toQuestionsReadDTO(Questions question) {
        return QuestionsReadDTO.builder()
                .questionId(question.getId())
                .question(question.getQuestions())
                .answer(question.getAnswers())
                .userAnswer(question.getUser_answers())
                .build();
    }

    private Map<String, List<String>> processKeywordsWithService(List<String> keywords, String text) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("keywords", keywords);
        requestBody.put("text", text);
        return openAIController.processKeywords(requestBody);
    }

    @Transactional
    public TestSheetSimpleReadDTO updateTestSheetBookmark(Long testsheetId) {
        TestSheet testSheet = testSheetRepo.findById(testsheetId)
                .orElseThrow(() -> new RuntimeException("TestSheet not found"));

        // Toggle the bookmark
        testSheet.setBookmark(!testSheet.isBookmark());
        testSheetRepo.save(testSheet);

        // Return the updated details
        return TestSheetSimpleReadDTO.toSimpleDTO(testSheet);
    }

    @Transactional
    public void deleteTestSheet(Long testsheetId) {
        TestSheet testSheet = testSheetRepo.findById(testsheetId)
                .orElseThrow(() -> new RuntimeException("TestSheet not found"));
        testSheetRepo.delete(testSheet);
    }
}
