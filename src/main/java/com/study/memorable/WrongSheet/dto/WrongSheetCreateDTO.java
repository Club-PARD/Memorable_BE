package com.study.memorable.WrongSheet.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class WrongSheetCreateDTO {
    private String questions1;
    private String answers1;
    private String questions2;
    private String answers2;
    private boolean bookmark;
    private Long test_id;
    private LocalDateTime created_date;
}
