package com.study.memorable.WrongSheet.dto;

import com.study.memorable.WrongSheet.entity.WrongSheet;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WrongSheetReadDTO {
    private Long id;
    private boolean bookmark;
    private String name;
    private String category;
    private String questions1;
    private String answers1;
    private String questions2;
    private String answers2;
    private LocalDateTime created_date;

    public static WrongSheetReadDTO toDTO(WrongSheet wrongSheet) {
        return WrongSheetReadDTO.builder()
                .id(wrongSheet.getId())
                .bookmark(wrongSheet.isBookmark())
                .name(wrongSheet.getFile().getFile_name())
                .category(wrongSheet.getFile().getCategory())
                .created_date(LocalDateTime.now())
                .build();
    }
}
