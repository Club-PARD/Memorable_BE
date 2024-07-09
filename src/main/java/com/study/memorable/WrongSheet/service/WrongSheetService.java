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
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WrongSheetService {
    private final WrongSheetRepo wrongSheetRepo;
    private final WrongSheetQuestionRepo wrongSheetQuestionRepo;
    private final QuestionsRepo questionsRepo;

    @Transactional
    public WrongSheetResponseDTO createWrongSheet(WrongSheetCreateDTO dto) {
        List<Long> questionIds = dto.getQuestions().stream()
                .map(WrongSheetCreateDTO.QuestionDTO::getQuestionId)
                .collect(Collectors.toList());

        List<Questions> questions = questionsRepo.findAllById(questionIds);

        if (questions.isEmpty()) {
            throw new RuntimeException("No questions found for the given IDs");
        }

        File file = questions.get(0).getFile();
        Optional<WrongSheet> existingWrongSheetOptional = wrongSheetRepo.findByFile(file);

        WrongSheet wrongSheet;
        if (existingWrongSheetOptional.isPresent()) {
            wrongSheet = existingWrongSheetOptional.get();
            List<Long> existingQuestionIds = wrongSheet.getQuestionIds().stream()
                    .map(Long::parseLong)
                    .collect(Collectors.toList());
            existingQuestionIds.addAll(questionIds);
            List<Long> updatedQuestionIds = existingQuestionIds.stream().distinct().sorted().collect(Collectors.toList());
            wrongSheet.setQuestionIds(updatedQuestionIds.stream().map(String::valueOf).collect(Collectors.toList()));

            // 새로운 질문만 추가
            List<Questions> newQuestions = questions.stream()
                    .filter(question -> !existingQuestionIds.contains(question.getId()))
                    .toList();

            List<WrongSheetQuestion> newWrongSheetQuestions = newQuestions.stream()
                    .map(question -> WrongSheetQuestion.builder()
                            .wrongSheet(wrongSheet)
                            .question(question)
                            .build())
                    .collect(Collectors.toList());

            wrongSheet.getWrongSheetQuestions().addAll(newWrongSheetQuestions);
            wrongSheetQuestionRepo.saveAll(newWrongSheetQuestions);
        } else {
            wrongSheet = WrongSheet.builder()
                    .file(file)
                    .bookmark(false)
                    .created_date(LocalDateTime.now())
                    .questionIds(questionIds.stream().sorted().map(String::valueOf).collect(Collectors.toList()))
                    .build();

            wrongSheetRepo.save(wrongSheet);

            List<WrongSheetQuestion> wrongSheetQuestions = questions.stream()
                    .map(question -> WrongSheetQuestion.builder()
                            .wrongSheet(wrongSheet)
                            .question(question)
                            .build())
                    .collect(Collectors.toList());

            wrongSheet.setWrongSheetQuestions(wrongSheetQuestions);
            wrongSheetQuestionRepo.saveAll(wrongSheetQuestions);
        }

        return WrongSheetResponseDTO.toDTO(wrongSheet);
    }

    @Transactional(readOnly = true)
    public List<WrongSheetSimpleReadDTO> getWrongSheetsByUserId(String userId) {
        List<WrongSheet> wrongSheets = wrongSheetRepo.findByUserId(userId);
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
    public WrongSheetSimpleReadDTO toggleBookmark(Long wrongsheetId) {
        WrongSheet wrongSheet = wrongSheetRepo.findById(wrongsheetId)
                .orElseThrow(() -> new RuntimeException("WrongSheet not found"));
        wrongSheet.setBookmark(!wrongSheet.isBookmark());
        wrongSheetRepo.save(wrongSheet);
        return WrongSheetSimpleReadDTO.toSimpleDTO(wrongSheet);
    }

    @Transactional
    public void deleteWrongSheet(Long wrongSheetId) {
        wrongSheetRepo.deleteById(wrongSheetId);
    }
}
