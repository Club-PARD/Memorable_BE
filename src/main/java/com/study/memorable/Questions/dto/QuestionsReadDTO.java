package com.study.memorable.Questions.dto;

import com.study.memorable.Questions.entity.Questions;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionsReadDTO {

    private Long questionId;
    private String question;
    private String answer;
    private String userAnswer;

    public static QuestionsReadDTO toDTO(Questions question) {
        return QuestionsReadDTO.builder()
                .questionId(question.getId())
                .question(question.getQuestions())
                .answer(question.getAnswers())
                .userAnswer(question.getUser_answers())
                .build();
    }
}
