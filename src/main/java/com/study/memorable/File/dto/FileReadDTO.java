package com.study.memorable.File.dto;

import com.study.memorable.File.entity.File;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FileReadDTO {

    private Long id;
    private String file_name;
    private String category;
    private String content;
    private List<String> keyword1;
    private List<String> keyword2;
    private LocalDateTime created_date;

    public static FileReadDTO toDTO(File file){
        return FileReadDTO.builder()
                .id(file.getId())
                .file_name(file.getFile_name())
                .category(file.getCategory())
                .content(file.getContent())
                .keyword1(file.getKeyword1())
                .keyword2(file.getKeyword2())
                .created_date(LocalDateTime.now())
                .build();
    }

}
