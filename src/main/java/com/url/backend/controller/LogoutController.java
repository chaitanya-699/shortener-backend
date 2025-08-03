package com.url.backend.controller;


import com.url.backend.DTO.UserDto;
import com.url.backend.service.LoginService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/logout")
public class LogoutController {

    @Autowired
    LoginService loginService;
    @GetMapping
    public ResponseEntity<UserDto> logout(HttpServletRequest request, HttpServletResponse response){
        return loginService.logout(request, response);
    }
}
