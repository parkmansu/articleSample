package com.example.articlesample.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserCheckRequestDto {

    private String username;
    private String password;
    private String pwdCheck;

}
