package com.study.memorable.WorkSheet.dto;

import com.study.memorable.WorkSheet.entity.WorkSheet;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WorkSheetReadDTO {
    private Long id;
    private String name;
    private String category;
    private boolean bookmark;
    private Boolean isCompleteAllBlanks;
    private Boolean isReExtracted;
    private List<String> answer1;
    private List<String> answer2;
    private LocalDateTime created_date;

    public static WorkSheetReadDTO toDTO(WorkSheet worksheet) {
        return WorkSheetReadDTO.builder()
                .id(worksheet.getId())
                .name(worksheet.getFile().getFile_name())
                .category(worksheet.getFile().getCategory())
                .bookmark(worksheet.isBookmark())
                .isCompleteAllBlanks(worksheet.isCompleteAllBlanks())
                .isReExtracted(worksheet.isReExtracted())
                .answer1(worksheet.getAnswer1())
                .answer2(worksheet.getAnswer2())
                .created_date(worksheet.getCreated_date())
                .build();
    }
}
