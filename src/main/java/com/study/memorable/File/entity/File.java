package com.study.memorable.File.entity;

import com.study.memorable.File.dto.FileCreateDTO;
import com.study.memorable.Questions.entity.Questions;
import com.study.memorable.TestSheet.entity.TestSheet;
import com.study.memorable.User.entity.User;
import com.study.memorable.WorkSheet.entity.WorkSheet;
import com.study.memorable.WrongSheet.entity.WrongSheet;
import jakarta.persistence.*;
import lombok.*;
import com.study.memorable.config.ListStringConverter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Entity
@Getter
@Setter
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

    @Convert(converter = ListStringConverter.class)
    private List<String> keyword;

    private LocalDateTime created_date;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "file", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WorkSheet> worksheets;

    @OneToMany(mappedBy = "file", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TestSheet> testSheets;

    @OneToMany(mappedBy = "file", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WrongSheet> wrongSheets;

    @OneToMany(mappedBy = "file", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Questions> questions;

    public static File toEntity(FileCreateDTO dto, User user) {
        return File.builder()
                .file_name(dto.getFileName())
                .category(dto.getCategory())
                .content(dto.getContent())
                .created_date(LocalDateTime.now())
                .user(user)
                .build();
    }

    public List<String> getOddIndexKeywords() {
        if (keyword == null || keyword.isEmpty()) {
            return List.of();
        }
        return IntStream.range(0, keyword.size())
                .filter(i -> i % 2 == 1)
                .mapToObj(keyword::get)
                .collect(Collectors.toList());
    }

    public List<String> getEvenIndexKeywords() {
        if (keyword == null || keyword.isEmpty()) {
            return List.of();
        }
        return IntStream.range(0, keyword.size())
                .filter(i -> i % 2 == 0)
                .mapToObj(keyword::get)
                .collect(Collectors.toList());
    }

    public void tests() {
        List<String> a = getOddIndexKeywords();
        List<String> b = getEvenIndexKeywords();
        log.info("1: " + a);
        log.info("2: " + b);
    }
}
