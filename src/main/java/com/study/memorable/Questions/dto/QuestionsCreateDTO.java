package com.study.memorable.Questions.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionsCreateDTO {
    private String questions;
    private String answers;
    private String user_answers;
    private Long fileId;
}