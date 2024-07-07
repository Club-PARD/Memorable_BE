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
    private final FileRepo fileRepo;
    private final WorkSheetRepo workSheetRepo;
    private final QuestionsRepo questionsRepo;
    private final OpenAIController openAIController;

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
                .build();
        testSheetRepo.save(testSheet);

        return TestSheetReadDTO.toDTO(testSheet, questions1, questions2);
    }

    @Transactional
    public void updateUserAnswers(Long testsheetId, boolean isReExtracted, List<Boolean> isCompleteAllBlanks, List<String> userAnswers1, List<String> userAnswers2) {
        TestSheet testSheet = testSheetRepo.findById(testsheetId)
                .orElseThrow(() -> new RuntimeException("TestSheet not found"));

        // Update test sheet attributes
        testSheet.setReExtracted(isReExtracted);
        testSheet.setCompleteAllBlanks(isCompleteAllBlanks);

        // Fetch the questions for the test sheet
        File file = testSheet.getFile();
        List<Questions> questions = questionsRepo.findByFile(file);

        if (isCompleteAllBlanks.get(0)) {
            for (int i = 0; i < userAnswers1.size(); i++) {
                Questions question = questions.get(i);
                question.setUser_answers(userAnswers1.get(i));
                questionsRepo.save(question);
            }
        }

        if (isCompleteAllBlanks.get(1)) {
            for (int i = 0; i < userAnswers2.size(); i++) {
                Questions question = questions.get(i + 20);
                question.setUser_answers(userAnswers2.get(i));
                questionsRepo.save(question);
            }
        }
    }

    @Transactional
    public TestSheetUpdateDTO updateUserAnswers(Long testsheetId, TestSheetUpdateDTO userAnswersDTO) {
        TestSheet testSheet = testSheetRepo.findById(testsheetId)
                .orElseThrow(() -> new RuntimeException("TestSheet not found"));

        testSheet.setReExtracted(userAnswersDTO.isReExtracted());
        testSheet.setCompleteAllBlanks(userAnswersDTO.getIsCompleteAllBlanks());

        File file = testSheet.getFile();
        List<Questions> questions = questionsRepo.findByFile(file);

        List<String> allQuestions = new ArrayList<>();
        List<String> allAnswers = new ArrayList<>();
        List<String> allUserAnswers = new ArrayList<>();

        if (userAnswersDTO.getIsCompleteAllBlanks().get(0)) {
            for (int i = 0; i < userAnswersDTO.getUserAnswers1().size(); i++) {
                Questions question = questions.get(i);
                question.setUser_answers(userAnswersDTO.getUserAnswers1().get(i));
                questionsRepo.save(question);
            }
        }

        if (userAnswersDTO.getIsCompleteAllBlanks().get(1)) {
            for (int i = 0; i < userAnswersDTO.getUserAnswers2().size(); i++) {
                Questions question = questions.get(i + 20);
                question.setUser_answers(userAnswersDTO.getUserAnswers2().get(i));
                questionsRepo.save(question);
            }
        }

        // Create a request body for the API call
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("content", file.getContent());
        requestBody.put("questions", allQuestions);
        requestBody.put("answers", allAnswers);
        requestBody.put("userAnswers", allUserAnswers);

        // Call the scoreAnswers API
        Map<String, Object> scoringResult = openAIController.scoreAnswers(requestBody);

        testSheet.setScore((Integer) scoringResult.get("score"));
        testSheet.setIsCorrect((List<Boolean>) scoringResult.get("isCorrect"));

        testSheetRepo.save(testSheet);

        return TestSheetUpdateDTO.builder()
//                .isReExtracted(testSheet.isReExtracted())
//                .isCompleteAllBlanks(testSheet.getIsCompleteAllBlanks())
                .score(testSheet.getScore())
                .isCorrect(testSheet.getIsCorrect())
                .build();
    }

//    private Map<String, List<String>> parseResponseToQuestionsAndAnswers(String response) {
//        String[] lines = response.split("\n");
//        List<String> questions = new ArrayList<>();
//        List<String> answers = new ArrayList<>();
//
//        for (int i = 0; i < lines.length; i++) {
//            if (lines[i].startsWith("Q: ")) {
//                String question = lines[i].replace("Q: ", "").trim();
//                if (i + 1 < lines.length && lines[i + 1].startsWith("A: ")) {
//                    String answer = lines[i + 1].replace("A: ", "").trim();
//                    questions.add(question);
//                    answers.add(answer);
//                    i++; // Skip next line since it's the answer line
//                }
//            }
//        }
//
//        Map<String, List<String>> result = new HashMap<>();
//        result.put("questions", questions);
//        result.put("answers", answers);
//        return result;
//    }

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

        List<QuestionsReadDTO> questions1 = questions.stream()
                .limit(20)
                .map(this::toQuestionsReadDTO)
                .collect(Collectors.toList());

        List<QuestionsReadDTO> questions2 = questions.stream()
                .skip(20)
                .limit(20)
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