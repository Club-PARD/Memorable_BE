package com.study.memorable.TestSheet.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.study.memorable.Questions.dto.QuestionsReadDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestSheetResponseDTO {
    private Long testsheetId;
    private String name;
    private String category;

    @JsonProperty("isReExtracted")
    private boolean isReExtracted;
    private List<QuestionsReadDTO> questions1;
    private List<QuestionsReadDTO> questions2;
}
