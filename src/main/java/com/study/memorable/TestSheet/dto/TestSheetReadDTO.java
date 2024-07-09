package com.study.memorable.TestSheet.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.study.memorable.Questions.dto.QuestionsReadDTO;
import com.study.memorable.TestSheet.entity.TestSheet;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TestSheetReadDTO {
    private Long testsheetId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String name;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String category;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("isReExtracted")
    private boolean isReExtracted;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Boolean> isCompleteAllBlanks;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private boolean testsheetBookmark;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<QuestionsReadDTO> questions1;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<QuestionsReadDTO> questions2;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDateTime created_date;

    public static TestSheetReadDTO toDTO(TestSheet testSheet, List<QuestionsReadDTO> questions1, List<QuestionsReadDTO> questions2) {
        return TestSheetReadDTO.builder()
                .testsheetId(testSheet.getId())
                .name(testSheet.getFile().getFile_name())
                .category(testSheet.getFile().getCategory())
                .isReExtracted(testSheet.isReExtracted())
                .isCompleteAllBlanks(testSheet.getIsCompleteAllBlanks())
                .questions1(questions1)
                .questions2(questions2)
                .build();
    }
}