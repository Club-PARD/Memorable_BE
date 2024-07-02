package com.study.memorable.User.dto;


import com.study.memorable.User.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserReadDTO {
    private Long id;
    private String email;
    private String given_name;
    private String family_name;
    private LocalDateTime create_date;

    public static UserReadDTO toDTO(User user){
        return UserReadDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .given_name(user.getGiven_name())
                .family_name(user.getFamily_name())
                .create_date(user.getCreate_date())
                .build();
    }
}
