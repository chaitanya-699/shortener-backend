package com.url.backend.DTO;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginDto {

    private String email;
    private String password;

    public LoginDto(){

    }

}
