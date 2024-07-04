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
    private LocalDateTime wrongsheetCreate_date;

    public static WrongSheetReadDTO toDTO(WrongSheet wrongSheet) {
        return WrongSheetReadDTO.builder()
                .id(wrongSheet.getId())
                .bookmark(wrongSheet.isBookmark())
                .name(wrongSheet.getTestSheet().getFile().getFile_name())
                .category(wrongSheet.getTestSheet().getFile().getCategory())
                .questions1(wrongSheet.getQuestions1())
                .answers1(wrongSheet.getAnswers1())
                .questions2(wrongSheet.getQuestions2())
                .answers2(wrongSheet.getAnswers2())
                .wrongsheetCreate_date(LocalDateTime.now())
                .build();
    }
}
