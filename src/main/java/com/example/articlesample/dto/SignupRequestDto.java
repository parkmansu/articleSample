package com.example.articlesample.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class SignupRequestDto {

    private Long id;
    private String username;
    private String password;
    private String nickname;
}
