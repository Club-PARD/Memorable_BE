package com.study.memorable.TestSheet.entity;

import com.study.memorable.File.entity.File;
import com.study.memorable.TestSheet.dto.TestSheetCreateDTO;
import com.study.memorable.WrongSheet.entity.WrongSheet;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TestSheet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "file_id")
    private File file;

    private boolean bookmark;
    private boolean isReExtracted;

    private LocalDateTime created_date;

    public static TestSheet toEntity(TestSheetCreateDTO dto, File file){
        return TestSheet.builder()
                .file(file)
                .bookmark(dto.isBookmark())
                .created_date(LocalDateTime.now())
                .build();
    }
}