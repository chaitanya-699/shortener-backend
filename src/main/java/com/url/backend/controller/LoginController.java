package com.url.backend.controller;


import com.url.backend.DTO.*;
import com.url.backend.service.LoginService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth/login")
public class LoginController {

    @Autowired
    LoginService loginService;

    @PostMapping
    public ResponseEntity<UserDto> login(@RequestBody LoginDto loginDto, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
            String email = loginDto.getEmail();
            String password = loginDto.getPassword();
            return loginService.login(email, password, httpServletRequest, httpServletResponse);
    }
    @GetMapping("/me")
    public ResponseEntity<UserDto> tokenLogin(HttpServletRequest httpServletRequest){
        return loginService.validateToken(httpServletRequest);
    }

    @PostMapping("/forgotPassword")
    public ResponseEntity<OtpDto> forgotPass(@RequestBody ForgotPasswordRequest forgotPasswordRequest, HttpServletResponse response){
            String email = forgotPasswordRequest.getEmail();
            return loginService.forgotPass(email, response);
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<UserDto> updatePass(@RequestBody LoginDto loginDto, HttpServletRequest request, HttpServletResponse response){
        String email = loginDto.getEmail();
        String newPass = loginDto.getPassword();
        return loginService.resetPass(email, newPass, request, response);
    }

    @PostMapping("/verifyCode")
    public ResponseEntity<String> verifyCode(@RequestBody VerifyCodeDto verifyCodeDto, HttpServletResponse response){
        String email = verifyCodeDto.getEmail();
        String code = verifyCodeDto.getCode();
        return loginService.verifyOtp(email, code, response);
    }

    @PostMapping("/guest")
    public ResponseEntity<ServerMsgDto> guestLogin(@RequestBody GuestAllDto guestAllDto){
        String guestId = guestAllDto.getGuestId();
        return loginService.guestLogin(guestId);
    }

}
