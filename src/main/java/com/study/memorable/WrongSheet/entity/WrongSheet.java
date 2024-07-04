package com.study.memorable.WrongSheet.entity;

import com.study.memorable.TestSheet.entity.TestSheet;
import com.study.memorable.WrongSheet.dto.WrongSheetCreateDTO;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class WrongSheet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String questions1;
    private String answers1;
    private String questions2;
    private String answers2;
    private boolean bookmark;
    private LocalDateTime wrongsheetCreate_date;

    @ManyToOne
    @JoinColumn(name = "testSheet_id")
    private TestSheet testSheet;

    public static WrongSheet toEntity(WrongSheetCreateDTO dto, TestSheet testSheet) {
        return WrongSheet.builder()
                .questions1(dto.getQuestions1())
                .answers1(dto.getAnswers1())
                .questions2(dto.getQuestions2())
                .answers2(dto.getAnswers2())
                .wrongsheetCreate_date(LocalDateTime.now())
                .testSheet(testSheet)
                .build();
    }
}
