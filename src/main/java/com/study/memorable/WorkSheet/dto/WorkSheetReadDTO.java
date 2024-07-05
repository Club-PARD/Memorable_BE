package com.study.memorable.WorkSheet.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
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
@JsonPropertyOrder({ "worksheetId", "name", "category", "isCompleteAllBlanks", "isReExtracted", "answer1", "answer2" })
public class WorkSheetReadDTO {
    private Long worksheetId;
    private String name;
    private String category;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean isCompleteAllBlanks;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean isAddWorkSheet;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<String> answer1;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<String> answer2;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean worksheetBookmark;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDateTime worksheetCreate_date;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String content;

    // 기본 필드만 포함한 DTO
    public static WorkSheetReadDTO toBasicDTO(WorkSheet worksheet) {
        return WorkSheetReadDTO.builder()
                .worksheetId(worksheet.getId())
                .name(worksheet.getName())
                .category(worksheet.getFile().getCategory())
                .worksheetBookmark(worksheet.isBookmark())
                .worksheetCreate_date(worksheet.getCreated_date())
                .build();
    }

    // 모든 필드를 포함한 DTO
    public static WorkSheetReadDTO toFullDTO(WorkSheet worksheet) {
        return WorkSheetReadDTO.builder()
                .worksheetId(worksheet.getId())
                .name(worksheet.getName())
                .category(worksheet.getFile().getCategory())
                .isCompleteAllBlanks(worksheet.isCompleteAllBlanks())
                .isAddWorkSheet(worksheet.isAddWorksheet())
                .answer1(worksheet.getAnswer1())
                .answer2(worksheet.getAnswer2())
                .content(worksheet.getFile().getContent())
                .build();
    }
}