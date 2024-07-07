package com.study.memorable.TestSheet.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestSheetUpdateDTO {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private boolean isReExtracted;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Boolean> isCompleteAllBlanks;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<String> userAnswers1;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<String> userAnswers2;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private int score;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Boolean> isCorrect;
}
