package com.study.memorable.WorkSheet.dto;

import jakarta.persistence.Entity;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class WorkSheetCreateDTO {

    private List<String> answer1;
    private List<String> answer2;
    private boolean bookmark;

    private int file_id;
    private boolean isReExtracted;
    private boolean isCompleteAllBlanks;
    private LocalDateTime created_date;


}
