package com.study.memorable.WrongSheet.entity;

import com.study.memorable.File.entity.File;
import com.study.memorable.config.ListStringConverter;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

    @OneToMany(mappedBy = "wrongSheet", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WrongSheetQuestion> wrongSheetQuestions = new ArrayList<>();

    @Convert(converter = ListStringConverter.class)
    @Column(columnDefinition = "TEXT")
    private List<String> questionIds;
}
