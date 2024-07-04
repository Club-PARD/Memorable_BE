package com.study.memorable.Questions.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionsReadDTO {

    private Long id;
    private String questions;
    private String answers;
    private String user_answers;
}
