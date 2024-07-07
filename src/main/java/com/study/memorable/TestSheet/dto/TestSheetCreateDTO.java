package com.study.memorable.TestSheet.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestSheetCreateDTO {
    private Long fileId;
    private boolean bookmark;
    private List<Boolean> isCompleteAllBlanks;
    private boolean isReExtracted;
}