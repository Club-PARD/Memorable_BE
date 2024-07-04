package com.study.memorable.User.dto;

import lombok.Getter;

@Getter
public class UserCreateDTO {
    private String identifier;
    private String email;
    private String givenName;
    private String familyName;
}
