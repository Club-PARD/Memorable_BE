package com.study.memorable.TestSheet.service;

import com.study.memorable.File.entity.File;
import com.study.memorable.File.repo.FileRepo;
import com.study.memorable.Questions.dto.QuestionsCreateDTO;
import com.study.memorable.Questions.dto.QuestionsReadDTO;
import com.study.memorable.Questions.entity.Questions;
import com.study.memorable.Questions.Repo.QuestionsRepo;
import com.study.memorable.TestSheet.dto.TestSheetReadDTO;
import com.study.memorable.TestSheet.entity.TestSheet;
import com.study.memorable.TestSheet.repo.TestSheetRepo;
import com.study.memorable.WorkSheet.entity.WorkSheet;
import com.study.memorable.WorkSheet.repo.WorkSheetRepo;
import com.study.memorable.OpenAI.controller.OpenAIController;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        List<String> secondHalfKeywords = keywords.subList(halfSize, keywords.size());

        List<Questions> firstQuestions = generateAndSaveQuestions(firstHalfKeywords, file);
        List<Questions> secondQuestions = generateAndSaveQuestions(secondHalfKeywords, file);

        List<QuestionsReadDTO> questions1 = firstQuestions.stream()
                .map(this::toQuestionsReadDTO)
                .collect(Collectors.toList());

        List<QuestionsReadDTO> questions2 = secondQuestions.stream()
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

    private List<Questions> generateAndSaveQuestions(List<String> keywords, File file) {
        List<String> questions = processKeywordsWithService(keywords, file.getContent());
        return saveQuestions(questions, file);
    }

    private List<String> processKeywordsWithService(List<String> keywords, String text) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("keywords", keywords);
        requestBody.put("text", text);
        return openAIController.processKeywords(requestBody);
    }

    private List<Questions> saveQuestions(List<String> questions, File file) {
        return questions.stream().map(question -> {
            QuestionsCreateDTO dto = QuestionsCreateDTO.builder()
                    .questions(question)
                    .answers("정답")
                    .user_answers("")
                    .fileId(file.getId())
                    .build();
            Questions questionEntity = Questions.toEntity(dto, file);
            return questionsRepo.save(questionEntity);
        }).collect(Collectors.toList());
    }

    private QuestionsReadDTO toQuestionsReadDTO(Questions question) {
        return QuestionsReadDTO.builder()
                .id(question.getId())
                .questions(question.getQuestions())
                .answers(question.getAnswers())
                .user_answers(question.getUser_answers())
                .build();
    }
}
