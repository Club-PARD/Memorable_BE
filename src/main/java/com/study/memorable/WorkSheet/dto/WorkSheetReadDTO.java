package com.study.memorable.WorkSheet.dto;

import com.study.memorable.WorkSheet.entity.WorkSheet;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkSheetReadDTO {
    private Long id;
    private List<String> answer1;
    private List<String> answer2;
    private boolean bookmark;

    private int file_id;
    private boolean isReExtracted;
    private boolean isCompleteAllBlanks;
    private LocalDateTime created_date;


    public static WorkSheetReadDTO toDTO(WorkSheet workSheet){
        return WorkSheetReadDTO.builder()
                .id(workSheet.getId())
                .answer1(workSheet.getAnswer1())
                .answer2(workSheet.getAnswer2())
                .bookmark(workSheet.isBookmark())
//                .file_id(workSheet.getFile_id())
                .isReExtracted(workSheet.isReExtracted())
                .isCompleteAllBlanks(workSheet.isCompleteAllBlanks())
                .created_date(LocalDateTime.now())
                .build();
    }
}
