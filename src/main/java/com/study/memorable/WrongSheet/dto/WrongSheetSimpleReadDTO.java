package com.study.memorable.WrongSheet.dto;

import com.study.memorable.WrongSheet.entity.WrongSheet;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class WrongSheetSimpleReadDTO {
    private Long wrongsheetId;
    private String name;
    private String category;
    private boolean wrongsheetBookmark;
    private LocalDateTime wrongsheetCreateDate;

    public static WrongSheetSimpleReadDTO toSimpleDTO(WrongSheet wrongSheet) {
        return WrongSheetSimpleReadDTO.builder()
                .wrongsheetId(wrongSheet.getId())
                .name(wrongSheet.getFile().getFile_name())
                .category(wrongSheet.getFile().getCategory())
                .wrongsheetBookmark(wrongSheet.isBookmark())
                .wrongsheetCreateDate(wrongSheet.getCreated_date())
                .build();
    }
}
