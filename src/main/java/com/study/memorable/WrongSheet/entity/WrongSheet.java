package com.study.memorable.WrongSheet.entity;

import com.study.memorable.File.entity.File;
import com.study.memorable.TestSheet.entity.TestSheet;
import com.study.memorable.WrongSheet.dto.WrongSheetCreateDTO;
import com.study.memorable.WrongSheet.dto.WrongSheetReadDTO;
import jakarta.persistence.*;
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
public class WrongSheet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String questions1;
    private String answers1;
    private String questions2;
    private String answers2;
    private boolean bookmark;
    private LocalDateTime created_date;

    @ManyToOne
    @JoinColumn(name = "testSheet_id")
    private TestSheet testSheet;

    public WrongSheet toEntity(WrongSheetCreateDTO dto) {
        return WrongSheet.builder()
                .questions1(dto.getQuestions1())
                .answers1(dto.getAnswers1())
                .questions2(dto.getQuestions2())
                .answers2(dto.getAnswers2())
                .bookmark(dto.isBookmark())
                .created_date(LocalDateTime.now())
                .build();
    }
}
