package com.study.memorable.File.entity;

import com.study.memorable.File.dto.FileCreateDTO;
import com.study.memorable.TestSheet.entity.TestSheet;
import com.study.memorable.User.entity.User;
import com.study.memorable.WorkSheet.entity.WorkSheet;
import com.study.memorable.WrongSheet.entity.WrongSheet;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Embeddable
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String file_name;
    private String category;

    @Column(columnDefinition = "TEXT")
    private String content;
//    @Column(columnDefinition = "TEXT")
//    @ElementCollection
    @Convert(converter = ListStringConverter.class)
    private List<String> keyword1;
//    @ElementCollection
//    @Column(columnDefinition = "TEXT")
    @Convert(converter = ListStringConverter.class)
    private List<String> keyword2;
    private LocalDateTime date;

//    @ManyToOne
//    @JoinColumn(name = "user_id")
//    private User user;
//
//    @OneToMany(mappedBy = "file")
//    private List<WorkSheet> workSheets;
//
//    @OneToMany(mappedBy = "file")
//    private List<TestSheet> testSheets;
//
//    @OneToMany(mappedBy = "file")
//    private List<WrongSheet> wrongSheets;

    public File toEntity(FileCreateDTO dto){
        return File.builder()
                .file_name(dto.getFile_name())
                .category(dto.getCategory())
                .content(dto.getContent())
                .keyword1(dto.getKeyword1())
                .keyword2(dto.getKeyword2())
                .date(LocalDateTime.now())
                .build();
    }


    // 🔥하나의 컬럼에 string 배열을 담는 코드 ❗️
    @Converter
    public static class ListStringConverter implements AttributeConverter<List<String>, String>{
        @Override
        public String convertToDatabaseColumn(List<String> attribute) {
            if (attribute == null || attribute.isEmpty()) {
                return "";
            }
            return String.join(",", attribute);
        }
        @Override
        public List<String> convertToEntityAttribute(String dbData) {
            if (dbData == null || dbData.trim().isEmpty()) {
                return new ArrayList<>();
            }
            return Arrays.asList(dbData.split("\\s*,\\s*"));
        }
    }



}
