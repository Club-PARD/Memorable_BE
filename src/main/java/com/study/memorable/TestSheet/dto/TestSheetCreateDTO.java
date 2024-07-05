package com.study.memorable.TestSheet.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestSheetCreateDTO {
    private Long fileId;
    private boolean bookmark;
    private boolean isCompleteAllBlanks;
    private boolean isReExtracted;
}
