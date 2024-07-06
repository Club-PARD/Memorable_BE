package com.study.memorable.WrongSheet.entity;

import com.study.memorable.File.entity.File;
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
public class WrongSheet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean bookmark;
    private LocalDateTime created_date;

    @ManyToOne
    @JoinColumn(name = "file_id")
    private File file;

    @OneToMany(mappedBy = "wrongSheet", cascade = CascadeType.ALL)
    private List<WrongSheetQuestion> wrongSheetQuestions;
}
