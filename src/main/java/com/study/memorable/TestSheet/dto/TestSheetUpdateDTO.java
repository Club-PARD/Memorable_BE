package com.study.memorable.TestSheet.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.study.memorable.TestSheet.entity.TestSheet;
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
    private List<Boolean> isCompleteAllBlanks;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<String> userAnswers1;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<String> userAnswers2;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Integer> score;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Boolean> isCorrect;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("isReExtracted")
    private boolean isReExtracted;

    public static TestSheetUpdateDTO fromEntity(TestSheet testSheet) {
        return TestSheetUpdateDTO.builder()
                .isReExtracted(testSheet.isReExtracted())
                .isCompleteAllBlanks(testSheet.getIsCompleteAllBlanks())
                .score(testSheet.getScore())
                .isCorrect(testSheet.getIsCorrect())
                .build();
    }
}
