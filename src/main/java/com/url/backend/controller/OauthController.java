package com.url.backend.controller;

import com.url.backend.entity.User;
import com.url.backend.repo.UserRepo;
import com.url.backend.service.LoginService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth/oauth")
public class OauthController {

    @Autowired
    UserRepo userRepo;

    @Autowired
    LoginService loginService;

   @GetMapping("/handle")
   public void google(@AuthenticationPrincipal OAuth2User principal, OAuth2AuthenticationToken auth, HttpServletRequest request, HttpServletResponse response) throws IOException {

       String provider = auth.getAuthorizedClientRegistrationId();
       if ("google".equals(provider)) {
           loginService.googleLogin(principal, request, response);
       } else if ("github".equals(provider)) {
           loginService.githubLogin(principal, request, response);
       } else {
           response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "something went wrong");
       }
   }


}
