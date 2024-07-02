package com.study.memorable.TestSheet.entity;

import com.study.memorable.File.entity.File;
import com.study.memorable.TestSheet.dto.TestSheetCreateDTO;
import com.study.memorable.TestSheet.dto.TestSheetReadDTO;
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

    private String questions1;
    private String answers1;
    private String questions2;
    private String answers2;
    private String wrongAnswers;
    private boolean isCompleteTest;
    private boolean isNewTest;
    private String score;
    private LocalDateTime created_date;

    @ManyToOne
    @JoinColumn(name = "file_id")
    private File file;

    @OneToMany(mappedBy = "testSheet", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WrongSheet> wrongSheet;

    public TestSheet toEntity(TestSheetCreateDTO dto) {
        return TestSheet.builder()
                .questions1(dto.getQuestions1())
                .answers1(dto.getAnswers1())
                .questions2(dto.getQuestions2())
                .answers2(dto.getAnswers2())
                .wrongAnswers(dto.getWrongAnswers())
                .isCompleteTest(dto.isCompleteTest())
                .isNewTest(dto.isNewTest())
                .score(dto.getScore())
                .created_date(LocalDateTime.now())
                .build();
    }


}
