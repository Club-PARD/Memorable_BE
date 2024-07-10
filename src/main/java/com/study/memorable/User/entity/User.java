package com.study.memorable.User.entity;

import com.study.memorable.User.dto.UserCreateDTO;
import com.study.memorable.File.entity.File;
import com.study.memorable.User.dto.UserReadDTO;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

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

    @Column(unique = true)
    private String email;
    private String givenName;
    private String familyName;

    @CreatedDate
    private LocalDateTime created_date;

    @OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<File> files;

    public static User toEntity(UserCreateDTO dto){
        return User.builder()
                .id(dto.getIdentifier())
                .email(dto.getEmail())
                .givenName(dto.getGivenName())
                .familyName(dto.getFamilyName())
                .created_date(LocalDateTime.now())
                .build();
    }

}
