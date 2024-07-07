package com.study.memorable.Questions.entity;

import com.study.memorable.File.entity.File;
import com.study.memorable.Questions.dto.QuestionsCreateDTO;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Questions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String questions;
    private String answers;
    @Setter
    private String user_answers;

    @ManyToOne
    @JoinColumn(name = "file_id")
    private File file;

    public static Questions toEntity(QuestionsCreateDTO dto, File file) {
        return Questions.builder()
                .questions(dto.getQuestions())
                .answers(dto.getAnswers())
                .user_answers(dto.getUser_answers())
                .file(file)
                .build();
    }

    public void updateEntity(QuestionsCreateDTO dto) {
        this.questions = dto.getQuestions();
        this.answers = dto.getAnswers();
        this.user_answers = dto.getUser_answers();
    }

}
