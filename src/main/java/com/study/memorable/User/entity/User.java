package com.study.memorable.User.entity;

import com.study.memorable.User.dto.UserCreateDTO;
import com.study.memorable.File.entity.File;
import com.study.memorable.User.dto.UserReadDTO;
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
    private String id;
    private String email;
    private String givenName;
    private String familyName;
    private LocalDateTime created_date;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<File> files;

//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<WorkSheetClickLog> workSheetClickLogs;

    public static User toEntity(UserCreateDTO dto){
        return User.builder()
                .id(dto.getIdentifier()) // Changed from dto.getId() to dto.getIdentifier()
                .email(dto.getEmail())
                .givenName(dto.getGivenName())
                .familyName(dto.getFamilyName())
                .created_date(LocalDateTime.now())
                .build();
    }

}
