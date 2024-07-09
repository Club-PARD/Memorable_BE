package com.study.memorable.TestSheet.entity;

import com.study.memorable.File.entity.File;
import com.study.memorable.TestSheet.dto.TestSheetCreateDTO;
import com.study.memorable.config.ListBooleanConverter;
import com.study.memorable.config.ListIntegerConverter;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
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

    @Convert(converter = ListIntegerConverter.class)
    @Column(columnDefinition = "VARCHAR(20)")
    private List<Integer> score;

    @Convert(converter = ListBooleanConverter.class)
    @Column(columnDefinition = "TEXT")
    private List<Boolean> isCorrect;

    @PrePersist
    protected void onCreate() {
        if (this.isCorrect == null) {
            this.isCorrect = new ArrayList<>(Collections.nCopies(40, false));
        }
        if (this.score == null) {
            this.score = Arrays.asList(0, 0);
        }
    }

    public List<Boolean> getIsCorrect() {
        if (this.isCorrect == null || this.isCorrect.isEmpty()) {
            return new ArrayList<>(Collections.nCopies(40, false));
        }
        return this.isCorrect;
    }

    public List<Integer> getScore() {
        if (this.score == null || this.score.isEmpty()) {
            return Arrays.asList(0, 0);
        }
        return this.score;
    }

//    public boolean isReExtracted() {
//        return isReExtracted;
//    }

    public static TestSheet toEntity(TestSheetCreateDTO dto, File file) {
        return TestSheet.builder()
                .file(file)
                .bookmark(dto.isBookmark())
                .created_date(LocalDateTime.now())
                .isReExtracted(dto.isReExtracted())
                .isCompleteAllBlanks(dto.getIsCompleteAllBlanks())
                .score(Arrays.asList(0, 0))
                .isCorrect(new ArrayList<>(Collections.nCopies(40, false)))
                .build();
    }
}
