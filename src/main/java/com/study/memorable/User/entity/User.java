package com.study.memorable.User.entity;

import com.study.memorable.User.dto.UserCreateDTO;
import com.study.memorable.File.entity.File;
import com.study.memorable.WorkSheet.entity.WorkSheetClickLog;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String given_name;
    private String family_name;
    private LocalDateTime create_date;
//    private int authorization_code;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<File> files;

    // WorkSheet 클릭 로그 관련 코드
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WorkSheetClickLog> workSheetClickLogs;


    public User toEntity(UserCreateDTO dto){
        return User.builder()
                .email(dto.getEmail())
                .given_name(dto.getGiven_name())
                .family_name(dto.getFamily_name())
                .create_date(LocalDateTime.now())
                .build();
    }


}
