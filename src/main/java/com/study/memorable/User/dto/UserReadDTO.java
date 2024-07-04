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
    private String identifier;
    private String email;
    private String givenName;
    private String familyName;
    private LocalDateTime created_date;

    public static UserReadDTO toDTO(User user) {
        return UserReadDTO.builder()
                .identifier(user.getId())
                .email(user.getEmail())
                .givenName(user.getGivenName())
                .familyName(user.getFamilyName())
                .created_date(user.getCreated_date())
                .build();
    }
}
