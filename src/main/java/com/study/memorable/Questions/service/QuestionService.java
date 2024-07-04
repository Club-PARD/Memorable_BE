package com.study.memorable.Questions.service;

import com.study.memorable.Questions.Repo.QuestionsRepo;
import com.study.memorable.Questions.dto.QuestionsCreateDTO;
import com.study.memorable.Questions.dto.QuestionsReadDTO;
import com.study.memorable.Questions.entity.Questions;
import com.study.memorable.File.repo.FileRepo;
import com.study.memorable.File.entity.File;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionsRepo questionsRepo;
    private final FileRepo fileRepo;

    public QuestionsReadDTO createQuestions(QuestionsCreateDTO dto) {
        File file = fileRepo.findById(dto.getFileId()).orElseThrow(() -> new RuntimeException("File not found"));
        Questions questions = Questions.toEntity(dto, file);
        questionsRepo.save(questions);
        return toDTO(questions);
    }

    public QuestionsReadDTO getQuestions(Long id) {
        Questions questions = questionsRepo.findById(id).orElseThrow(() -> new RuntimeException("Questions not found"));
        return toDTO(questions);
    }

    public QuestionsReadDTO updateQuestions(Long id, QuestionsCreateDTO dto) {
        Questions questions = questionsRepo.findById(id).orElseThrow(() -> new RuntimeException("Questions not found"));
        questions.updateEntity(dto);
        questionsRepo.save(questions);
        return toDTO(questions);
    }

    public void deleteQuestions(Long id) {
        questionsRepo.deleteById(id);
    }

    private QuestionsReadDTO toDTO(Questions questions) {
        return QuestionsReadDTO.builder()
                .id(questions.getId())
                .questions(questions.getQuestions())
                .answers(questions.getAnswers())
                .user_answers(questions.getUser_answers())
                .build();
    }
}
