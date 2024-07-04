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
public class TestSheetReadDTO {
    private Long id;
    private boolean bookmark;
    private String name;
    private String category;
    private LocalDateTime created_date;

    public static TestSheetReadDTO toDTO(TestSheet testSheet) {
        return TestSheetReadDTO.builder()
                .id(testSheet.getId())
                .bookmark(testSheet.isBookmark())
                .name(testSheet.getFile().getFile_name())
                .category(testSheet.getFile().getCategory())
                .created_date(LocalDateTime.now())
                .build();
    }
}
