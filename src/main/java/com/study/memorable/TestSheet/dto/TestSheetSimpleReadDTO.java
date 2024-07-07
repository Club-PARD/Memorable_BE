package com.study.memorable.TestSheet.dto;

import com.study.memorable.TestSheet.entity.TestSheet;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TestSheetSimpleReadDTO {
    private Long testsheetId;
    private String name;  // File의 file_name
    private String category;  // File의 category
    private boolean testsheetBookmark;
    private LocalDateTime testsheetCreateDate;

    public static TestSheetSimpleReadDTO toSimpleDTO(TestSheet testSheet) {
        return TestSheetSimpleReadDTO.builder()
                .testsheetId(testSheet.getId())
                .name(testSheet.getFile().getFile_name())
                .category(testSheet.getFile().getCategory())
                .testsheetBookmark(testSheet.isBookmark())
                .testsheetCreateDate(testSheet.getCreated_date())
                .build();
    }
}