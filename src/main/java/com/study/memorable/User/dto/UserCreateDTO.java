package com.study.memorable.User.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UserCreateDTO
{
    private String email;
    private String given_name;
    private String family_name;
    private LocalDateTime date;
//    private int authorization_code;
}
