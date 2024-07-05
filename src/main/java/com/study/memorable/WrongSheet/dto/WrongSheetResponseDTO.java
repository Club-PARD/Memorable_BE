package com.study.memorable.WrongSheet.dto;

import com.study.memorable.Questions.entity.Questions;
import com.study.memorable.WrongSheet.entity.WrongSheet;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WrongSheetResponseDTO {
    private Long wrongsheetId;
    private String name;
    private String category;
    private List<QuestionResponseDTO> questions;

    @Getter
    @AllArgsConstructor
    public static class QuestionResponseDTO {
        private Long questionId;
        private String question;
        private String answer;
        private String userAnswer;
    }

    public static WrongSheetResponseDTO toDTO(WrongSheet wrongSheet) {
        List<QuestionResponseDTO> questionResponseDTOs = wrongSheet.getFile().getQuestions().stream()
                .map(question -> new QuestionResponseDTO(
                        question.getId(),
                        question.getQuestions(),
                        question.getAnswers(),
                        question.getUser_answers()
                ))
                .collect(Collectors.toList());

        return WrongSheetResponseDTO.builder()
                .wrongsheetId(wrongSheet.getId())
                .name(wrongSheet.getFile().getFile_name())
                .category(wrongSheet.getFile().getCategory())
                .questions(questionResponseDTOs)
                .build();
    }
}
