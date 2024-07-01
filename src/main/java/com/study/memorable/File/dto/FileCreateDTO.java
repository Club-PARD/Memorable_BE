package com.study.memorable.File.dto;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class FileCreateDTO {
    private String file_name;
    private String category;
    private String content;
    private List<String> keyword1;
    private List<String> keyword2;
    private LocalDateTime date;
}
