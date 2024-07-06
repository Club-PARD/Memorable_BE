package com.study.memorable.WrongSheet.entity;

import com.study.memorable.Questions.entity.Questions;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class WrongSheetQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "wrong_sheet_id")
    private WrongSheet wrongSheet;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Questions question;
}
