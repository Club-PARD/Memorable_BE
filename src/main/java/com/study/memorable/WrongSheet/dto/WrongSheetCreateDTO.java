package com.study.memorable.WrongSheet.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class WrongSheetCreateDTO {
    private Long testSheetId;
    private String questions1;
    private String answers1;
    private String questions2;
    private String answers2;
}
