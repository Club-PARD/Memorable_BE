package com.study.memorable.WorkSheet.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class WorkSheetCreateDTO {
    private Long fileId;
    private Boolean isCompleteAllBlanks;
    private Boolean isReExtracted;
    private List<String> answer1;
    private List<String> answer2;
}
