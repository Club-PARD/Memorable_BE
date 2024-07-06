package com.study.memorable.TestSheet.dto;

import com.study.memorable.Questions.dto.QuestionsReadDTO;
import com.study.memorable.TestSheet.entity.TestSheet;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TestSheetDetailReadDTO {
    private Long testsheetId;
    private String name;
    private String category;
    private boolean isReExtracted;
    private List<Boolean> isCompleteAllBlanks;
    private List<QuestionsReadDTO> questions1;
    private List<QuestionsReadDTO> questions2;

    public static TestSheetDetailReadDTO toDTO(TestSheet testSheet, List<QuestionsReadDTO> questions1, List<QuestionsReadDTO> questions2) {
        return TestSheetDetailReadDTO.builder()
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
