package com.url.backend.controller;


import com.url.backend.DTO.LoginDto;
import com.url.backend.DTO.UserDto;
import com.url.backend.service.LoginService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth/register")
public class RegisterController {
    @Autowired
    private LoginService loginService;
    @PostMapping
    public ResponseEntity<UserDto> register(@RequestBody LoginDto loginDto, HttpServletResponse httpServletResponse){
        String email = loginDto.getEmail();
        String password = loginDto.getPassword();
        return loginService.register(email, password, httpServletResponse);
    }
}
