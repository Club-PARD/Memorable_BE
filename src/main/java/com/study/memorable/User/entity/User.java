package com.study.memorable.User.entity;

import com.study.memorable.User.dto.UserCreateDTO;
import com.study.memorable.User.dto.UserReadDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;

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
    private LocalDateTime date;
//    private int authorization_code;

    public User toEntity(UserCreateDTO dto){
        return User.builder()
                .email(dto.getEmail())
                .given_name(dto.getGiven_name())
                .family_name(dto.getFamily_name())
                .date(LocalDateTime.now())
                .build();
    }


}
