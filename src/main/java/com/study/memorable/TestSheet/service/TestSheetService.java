package com.study.memorable.TestSheet.service;

import com.study.memorable.TestSheet.dto.TestSheetDetailReadDTO;
import com.study.memorable.TestSheet.dto.TestSheetReadDTO;
import com.study.memorable.TestSheet.dto.TestSheetSimpleReadDTO;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        int halfSize = keywords.size() / 2;
        List<String> firstHalfKeywords = keywords.subList(0, halfSize);
        log.info("##############firstHalfKeywords: " + firstHalfKeywords);
        List<String> secondHalfKeywords = keywords.subList(halfSize, keywords.size());
        log.info("$$$$$$$$$$$$$$firstHalfKeywords: " + secondHalfKeywords);

        Map<String, List<Questions>> firstQuestions = generateAndSaveQuestions(firstHalfKeywords, file);
        Map<String, List<Questions>> secondQuestions = generateAndSaveQuestions(secondHalfKeywords, file);

        List<QuestionsReadDTO> questions1 = firstQuestions.get("questions").stream()
                .map(this::toQuestionsReadDTO)
                .collect(Collectors.toList());

        List<QuestionsReadDTO> questions2 = secondQuestions.get("questions").stream()
                .map(this::toQuestionsReadDTO)
                .collect(Collectors.toList());

        TestSheet testSheet = TestSheet.builder()
                .file(file)
                .bookmark(false)
                .created_date(LocalDateTime.now())
                .build();
        testSheetRepo.save(testSheet);

        return TestSheetReadDTO.toDTO(testSheet, questions1, questions2);
    }

    @Transactional(readOnly = true)
    public List<TestSheetSimpleReadDTO> getTestSheetsByUserId(String userId) {
        log.info("\n\n\n\nddkdkjnckshjdbcajlshbfesdj######################################");
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

    private Map<String, List<Questions>> generateAndSaveQuestions(List<String> keywords, File file) {
        Map<String, List<String>> qaMap = processKeywordsWithService(keywords, file.getContent());
        return saveQuestions(qaMap, file);
    }

    private Map<String, List<String>> processKeywordsWithService(List<String> keywords, String text) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("keywords", keywords);
        requestBody.put("text", text);
        return openAIController.processKeywords(requestBody);
    }

    private Map<String, List<Questions>> saveQuestions(Map<String, List<String>> qaMap, File file) {
        List<String> questions = qaMap.get("questions");
        List<String> answers = qaMap.get("answers");

        List<Questions> savedQuestions = questions.stream().map(question -> {
            int index = questions.indexOf(question);
            String answer = answers.get(index);
            log.info("Saving question: {}, answer: {}", question, answer);
            QuestionsCreateDTO dto = QuestionsCreateDTO.builder()
                    .questions(question)
                    .answers(answer)
                    .user_answers("")
                    .fileId(file.getId())
                    .build();
            Questions questionEntity = Questions.toEntity(dto, file);
            return questionsRepo.save(questionEntity);
        }).collect(Collectors.toList());

        Map<String, List<Questions>> result = new HashMap<>();
        result.put("questions", savedQuestions);
        return result;
    }

    @Transactional
    public void updateUserAnswers(Long testsheetId, boolean isReExtracted, boolean isCompleteAllBlanks, List<String> userAnswers1, List<String> userAnswers2) {
        TestSheet testSheet = testSheetRepo.findById(testsheetId)
                .orElseThrow(() -> new RuntimeException("TestSheet not found"));

        testSheet.setReExtracted(!isReExtracted);
        testSheet.setCompleteAllBlanks(!isCompleteAllBlanks);

        File file = testSheet.getFile();
        List<Questions> questions = questionsRepo.findByFile(file);

        if (questions.size() < userAnswers1.size() + userAnswers2.size()) {
            throw new IllegalArgumentException("The provided answers exceed the number of questions available.");
        }

        // First 20 answers
        for (int i = 0; i < userAnswers1.size(); i++) {
            Questions question = questions.get(i);
            question.setUser_answers(userAnswers1.get(i));
            questionsRepo.save(question);
        }

        // Next 20 answers
        for (int i = 0; i < userAnswers2.size(); i++) {
            Questions question = questions.get(i + 20); // Offset by 20
            question.setUser_answers(userAnswers2.get(i));
            questionsRepo.save(question);
        }
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
