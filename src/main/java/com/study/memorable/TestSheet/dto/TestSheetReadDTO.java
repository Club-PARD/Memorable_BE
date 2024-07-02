package com.study.memorable.TestSheet.dto;

import com.study.memorable.TestSheet.entity.TestSheet;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TestSheetReadDTO {
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

    public static TestSheetReadDTO toDTO(TestSheet testSheet){
        return TestSheetReadDTO.builder()
                .id(testSheet.getId())
                .questions1(testSheet.getQuestions1())
                .answers1(testSheet.getAnswers1())
                .questions2(testSheet.getQuestions2())
                .answers2(testSheet.getAnswers2())
                .wrongAnswers(testSheet.getWrongAnswers())
                .isCompleteTest(testSheet.isCompleteTest())
                .isNewTest(testSheet.isNewTest())
                .score(testSheet.getScore())
                .created_date(LocalDateTime.now())
                .build();
    }
}
