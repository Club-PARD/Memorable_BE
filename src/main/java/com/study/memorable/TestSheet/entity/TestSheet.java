package com.study.memorable.TestSheet.entity;

import com.study.memorable.TestSheet.dto.TestSheetCreateDTO;
import com.study.memorable.TestSheet.dto.TestSheetReadDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class TestSheet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String questions1;
    private String answers1;
    private String questions2;
    private String answers2;
    private String wrongAnswers;

//    private Long file_id;
    private boolean isCompleteTest;
    private boolean isNewTest;
    private String score;
    private LocalDateTime created_date;

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
