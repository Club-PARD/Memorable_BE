package com.study.memorable.WrongSheet.entity;

import com.study.memorable.File.entity.File;
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

    private boolean bookmark;
    private LocalDateTime created_date;

    @ManyToOne
    @JoinColumn(name = "testSheet_id")
    private File file;

}
