package com.study.memorable.WrongSheet.entity;

import com.study.memorable.WrongSheet.dto.WrongSheetCreateDTO;
import com.study.memorable.WrongSheet.dto.WrongSheetReadDTO;
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
public class WrongSheet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String questions1;
    private String answers1;
    private String questions2;
    private String answers2;
    private boolean bookmark;

    private Long test_id;
    private LocalDateTime created_date;

    public WrongSheet toEntity(WrongSheetCreateDTO dto) {
        return WrongSheet.builder()
                .questions1(dto.getQuestions1())
                .answers1(dto.getAnswers1())
                .questions2(dto.getQuestions2())
                .answers2(dto.getAnswers2())
                .bookmark(dto.isBookmark())
                .test_id(dto.getTest_id())
                .created_date(LocalDateTime.now())
                .build();
    }
}
