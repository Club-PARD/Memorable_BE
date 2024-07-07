package com.study.memorable.File.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FileCreateDTO {
    private String name;
    private String category;
    private String content;
    private String userId;
}
