package com.study.memorable.TestSheet.entity;

import com.study.memorable.File.entity.File;
import com.study.memorable.TestSheet.dto.TestSheetCreateDTO;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
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

    @Setter
    private boolean bookmark;
    private boolean isReExtracted;

    @ElementCollection
    private List<Boolean> isCompleteAllBlanks;

    private LocalDateTime created_date;

    private int score;

    @ElementCollection
    private List<Boolean> isCorrect;

    public void setCompleteAllBlanks(List<Boolean> isCompleteAllBlanks) {
        this.isCompleteAllBlanks = isCompleteAllBlanks;
    }

    public static TestSheet toEntity(TestSheetCreateDTO dto, File file){
        return TestSheet.builder()
                .file(file)
                .bookmark(dto.isBookmark())
                .created_date(LocalDateTime.now())
                .isReExtracted(dto.isReExtracted())
                .isCompleteAllBlanks(dto.getIsCompleteAllBlanks())
                .score(0)
                .isCorrect(new ArrayList<>(Collections.nCopies(20, false)))
                .build();
    }
}
