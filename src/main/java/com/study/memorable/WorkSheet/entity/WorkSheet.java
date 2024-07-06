package com.study.memorable.WorkSheet.entity;

import com.study.memorable.File.entity.File;
import com.study.memorable.config.ListStringConverter;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
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

    @Column(name = "is_add_worksheet")
    private boolean isAddWorksheet;

    private boolean isCompleteAllBlanks;
    private LocalDateTime created_date;
    private LocalDateTime recent_date;

    private String name;

    @ManyToOne
    @JoinColumn(name = "file_id")
    private File file;

    // 새로운 필드 추가
    @Column(name = "is_make_test_sheet", nullable = false, columnDefinition = "boolean default true")
    private boolean isMakeTestSheet = true;
}
