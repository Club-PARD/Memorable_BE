package com.study.memorable.WrongSheet.dto;

import com.study.memorable.WrongSheet.entity.WrongSheet;
import com.study.memorable.WrongSheet.entity.WrongSheetQuestion;
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
        List<QuestionResponseDTO> questionResponseDTOs = wrongSheet.getWrongSheetQuestions().stream()
                .map(wsq -> new QuestionResponseDTO(
                        wsq.getQuestion().getId(),
                        wsq.getQuestion().getQuestions(),
                        wsq.getQuestion().getAnswers(),
                        wsq.getQuestion().getUser_answers()
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
