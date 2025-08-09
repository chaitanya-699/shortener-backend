package com.url.backend.controller;


import com.url.backend.DTO.*;
import com.url.backend.service.LoginService;
import com.url.backend.service.UrlTableService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/logged/urlTableData")
public class LoggedUrlTableController {

    private final LoginService loginService;
    private final UrlTableService urlTableService;

    @Autowired
    public LoggedUrlTableController(LoginService loginService, UrlTableService urlTableService){
        this.loginService = loginService;
        this.urlTableService = urlTableService;
    }

    @PostMapping("/user")
    public ResponseEntity<UrlDto> registerUrl(@RequestBody RegisterUrlDto registerUrlDto, HttpServletRequest request, HttpServletResponse response){
        ResponseEntity<UserDto> responseEntity = loginService.validateToken(request);
        if(responseEntity.getStatusCode().is2xxSuccessful()){
               return urlTableService.registerUrl(registerUrlDto, request, response);
        }
        else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new UrlDto("not authenticated"));
        }
    }

    @GetMapping("/userAll")
    public ResponseEntity<List<UrlDto>> getAllUserUrls(HttpServletRequest request, HttpServletResponse response){
        ResponseEntity<UserDto> responseEntity = loginService.validateToken(request);

        
        if(responseEntity.getStatusCode().is2xxSuccessful()){
            UserDto userDto = responseEntity.getBody();
            
            if (userDto == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ArrayList<>());
            }
            String userId = userDto.getUserId();
            System.out.println(userId);
            System.out.println(userDto.toString());
            return urlTableService.getAllUserUrls(userId);
        }
        else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ArrayList<>());
        }
    }

    @PostMapping("/delete")
    public ResponseEntity<ServerMsgDto> deleteURl(@RequestBody DeleteDto deleteDto, HttpServletRequest request){
        ResponseEntity<UserDto> userDtoResponseEntity =  loginService.validateToken(request);
        if(userDtoResponseEntity.getStatusCode().is2xxSuccessful()){
            String userId = deleteDto.getUserID();
            String urlCode = deleteDto.getUrlCode();
            return urlTableService.deleteUserUrl(userId, urlCode);
        }
        else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ServerMsgDto("not authorized, try login again"));
        }
    }

}
