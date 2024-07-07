package com.study.memorable.Questions.controller;

import com.study.memorable.Questions.dto.QuestionsCreateDTO;
import com.study.memorable.Questions.dto.QuestionsReadDTO;
import com.study.memorable.Questions.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/questions")
@RequiredArgsConstructor
public class QuestionsController {
    private final QuestionService questionsService;

    @PostMapping
    public ResponseEntity<QuestionsReadDTO> createQuestions(@RequestBody QuestionsCreateDTO dto) {
        QuestionsReadDTO createdQuestions = questionsService.createQuestions(dto);
        return ResponseEntity.ok(createdQuestions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuestionsReadDTO> getQuestions(@PathVariable Long id) {
        QuestionsReadDTO questions = questionsService.getQuestions(id);
        return ResponseEntity.ok(questions);
    }

    @PutMapping("/{id}")
    public ResponseEntity<QuestionsReadDTO> updateQuestions(@PathVariable Long id, @RequestBody QuestionsCreateDTO dto) {
        QuestionsReadDTO updatedQuestions = questionsService.updateQuestions(id, dto);
        return ResponseEntity.ok(updatedQuestions);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuestions(@PathVariable Long id) {
        questionsService.deleteQuestions(id);
        return ResponseEntity.noContent().build();
    }
}