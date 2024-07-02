package com.study.memorable.TestSheet.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class TestSheetCreateDTO {
    private String questions1;
    private String answers1;
    private String questions2;
    private String answers2;
    private String wrongAnswers;

    //    private Long file_id;
    private boolean isCompleteTest;
    private boolean isNewTest;
    private String score;
    private LocalDateTime created_date;
}
