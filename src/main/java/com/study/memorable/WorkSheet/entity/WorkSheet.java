package com.study.memorable.WorkSheet.entity;

import com.study.memorable.File.entity.File;
import com.study.memorable.WorkSheet.dto.WorkSheetCreateDTO;
import com.study.memorable.WorkSheet.service.WorkSheetService;
import com.study.memorable.config.ListStringConverter;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkSheet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Convert(converter = ListStringConverter.class)
    private List<String> answer1;

    @Convert(converter = ListStringConverter.class)
    private List<String> answer2;

    private boolean bookmark;
    private int file_id;
    private boolean isReExtracted;
    private boolean isCompleteAllBlanks;
    private LocalDateTime created_date;

    public WorkSheet toEntity(WorkSheetCreateDTO dto) {
        return WorkSheet.builder()
                .answer1(dto.getAnswer1())
                .answer2(dto.getAnswer2())
                .bookmark(dto.isBookmark())
                .isReExtracted(dto.isReExtracted())
                .isCompleteAllBlanks(dto.isCompleteAllBlanks())
                .created_date(LocalDateTime.now())
                .build();
    }
}
