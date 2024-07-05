package com.study.memorable.WrongSheet.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class WrongSheetCreateDTO {
    private List<QuestionDTO> questions;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QuestionDTO {
        private Long questionId;
    }
}
