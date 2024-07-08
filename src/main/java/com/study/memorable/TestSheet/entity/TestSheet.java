package com.study.memorable.TestSheet.entity;

import com.study.memorable.File.entity.File;
import com.study.memorable.TestSheet.dto.TestSheetCreateDTO;
import com.study.memorable.config.ListBooleanConverter;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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

    @Convert(converter = ListBooleanConverter.class)
    private List<Boolean> isCorrect;

    @PrePersist
    protected void onCreate() {
        if (this.isCorrect == null) {
            this.isCorrect = new ArrayList<>(Collections.nCopies(20, false));
        }
    }

    public List<Boolean> getIsCorrect() {
        if (this.isCorrect == null || this.isCorrect.isEmpty()) {
            return new ArrayList<>(Collections.nCopies(20, false));
        }
        return this.isCorrect;
    }

    public void setIsCorrect(List<Boolean> isCorrect) {
        this.isCorrect = isCorrect;
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
