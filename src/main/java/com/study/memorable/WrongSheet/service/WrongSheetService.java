package com.study.memorable.WrongSheet.service;

import com.study.memorable.File.entity.File;
import com.study.memorable.Questions.Repo.QuestionsRepo;
import com.study.memorable.Questions.entity.Questions;
import com.study.memorable.WrongSheet.dto.WrongSheetCreateDTO;
import com.study.memorable.WrongSheet.dto.WrongSheetResponseDTO;
import com.study.memorable.WrongSheet.dto.WrongSheetSimpleReadDTO;
import com.study.memorable.WrongSheet.entity.WrongSheet;
import com.study.memorable.WrongSheet.entity.WrongSheetQuestion;
import com.study.memorable.WrongSheet.repo.WrongSheetRepo;
import com.study.memorable.WrongSheet.repo.WrongSheetQuestionRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WrongSheetService {
    private final WrongSheetRepo wrongSheetRepo;
    private final WrongSheetQuestionRepo wrongSheetQuestionRepo;
    private final QuestionsRepo questionsRepo;

    @Transactional
    public WrongSheetResponseDTO createWrongSheet(WrongSheetCreateDTO dto) {
        List<Questions> questions = questionsRepo.findAllById(dto.getQuestions().stream()
                .map(WrongSheetCreateDTO.QuestionDTO::getQuestionId)
                .collect(Collectors.toList()));

        File file = questions.get(0).getFile(); // Assuming all questions belong to the same file

        WrongSheet wrongSheet = WrongSheet.builder()
                .file(file)
                .bookmark(false)
                .created_date(LocalDateTime.now())
                .build();
        wrongSheetRepo.save(wrongSheet);

        List<WrongSheetQuestion> wrongSheetQuestions = questions.stream()
                .map(question -> WrongSheetQuestion.builder()
                        .wrongSheet(wrongSheet)
                        .question(question)
                        .build())
                .collect(Collectors.toList());
        wrongSheetQuestionRepo.saveAll(wrongSheetQuestions);

        wrongSheet.setWrongSheetQuestions(wrongSheetQuestions);

        return WrongSheetResponseDTO.toDTO(wrongSheet);
    }

    @Transactional(readOnly = true)
    public List<WrongSheetSimpleReadDTO> getWrongSheetsByUserId(String userId) {
        List<WrongSheet> wrongSheets = wrongSheetRepo.findByUserId(Long.valueOf(userId));
        return wrongSheets.stream()
                .map(WrongSheetSimpleReadDTO::toSimpleDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public WrongSheetResponseDTO getWrongSheetById(Long wrongsheetId) {
        WrongSheet wrongSheet = wrongSheetRepo.findById(wrongsheetId)
                .orElseThrow(() -> new RuntimeException("WrongSheet not found"));
        return WrongSheetResponseDTO.toDTO(wrongSheet);
    }

    @Transactional
    public void deleteWrongSheet(Long wrongSheetId) {
        wrongSheetRepo.deleteById(wrongSheetId);
    }
}
