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
public class WrongSheetReadDTO {
    private Long id;

    private String questions1;
    private String answers1;
    private String questions2;
    private String answers2;
    private boolean bookmark;
    private LocalDateTime created_date;

    public static WrongSheetReadDTO toDTO(WrongSheet wrongSheet) {
        return WrongSheetReadDTO.builder()
                .id(wrongSheet.getId())
                .questions1(wrongSheet.getQuestions1())
                .answers1(wrongSheet.getAnswers1())
                .questions2(wrongSheet.getQuestions2())
                .answers2(wrongSheet.getAnswers2())
                .bookmark(wrongSheet.isBookmark())
                .created_date(wrongSheet.getCreated_date())
                .build();
    }
}
