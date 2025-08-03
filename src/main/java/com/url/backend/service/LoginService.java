package com.url.backend.service;

import com.url.backend.DTO.OtpDto;
import com.url.backend.DTO.ServerMsgDto;
import com.url.backend.DTO.UserDto;
import com.url.backend.entity.GuestUrlTable;
import com.url.backend.entity.OtpEntry;
import com.url.backend.repo.GuestUrlRepo;
import com.url.backend.repo.OtpRepo;
import com.url.backend.util.JwtUtil;
import com.url.backend.entity.User;
import com.url.backend.repo.UserRepo;
import com.url.backend.util.PasswordUtil;
import com.url.backend.util.UrlCodeGenerator;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;

@Service
public class LoginService {
    private final UserRepo userRepo;
    private final JwtUtil jwtUtil;
    private final OtpService otpService;
    private final OtpRepo otpRepo;
    private final GuestUrlRepo guestUrlRepo;

    @Autowired
    public LoginService(UserRepo userRepo, JwtUtil jwtUtil,OtpService otpService , OtpRepo otpRepo, GuestUrlRepo guestUrlRepo){
        this.userRepo = userRepo;
        this.jwtUtil = jwtUtil;
        this.otpService = otpService ;
        this.otpRepo = otpRepo;
        this.guestUrlRepo = guestUrlRepo;
    }


    //helper function for checking already user registered or not
    public boolean alreadyUser(String email){
        return userRepo.findByEmail(email) != null;
    }
    //helper function for extracting name for email
    public String extractNameFromMail(String email){
        if (email == null || !email.contains("@")) {
            return "";
        }
        return email.substring(0, email.indexOf("@"));
    }

    public ResponseEntity<UserDto> register(String email, String password, HttpServletResponse httpServletResponse) {
        if(alreadyUser(email)){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new UserDto("Email already registered"));
        }
        else{
            String name = extractNameFromMail(email);
            User user1 = new User();
            user1.setPassword(PasswordUtil.encode(password));
            user1.setEmail(email);
            user1.setName(name);
            user1.setUserId(UrlCodeGenerator.generateUserId(email));
            userRepo.save(user1);

            User user = userRepo.findByEmail(email);

            String token = jwtUtil.generateToken(email);
            Cookie jwtCookie = new Cookie("jwt",token);
            jwtCookie.setHttpOnly(true);
            jwtCookie.setSecure(false);
            jwtCookie.setPath("/");
            jwtCookie.setMaxAge(24 * 60 * 60);
            httpServletResponse.addCookie(jwtCookie);
            UserDto userDto = new UserDto();
            userDto.setName(name);
            userDto.setEmail(email);
            userDto.setUserId(user.getUserId());
            userDto.setMessage("Registration successful");

            return ResponseEntity.status(HttpStatus.CREATED).body(userDto);
        }
    }

    public ResponseEntity<UserDto> login(String email, String password, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        if(!alreadyUser(email)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new UserDto("Email not registered"));
        }

        User user = userRepo.findByEmail(email);
        if(PasswordUtil.matches(password, user.getPassword())){
            user.setLogout(false);
            userRepo.save(user);
            String token = jwtUtil.generateToken(email);
            Cookie jwtCookie = new Cookie("jwt",token);
            jwtCookie.setHttpOnly(true);
            jwtCookie.setSecure(false);
            jwtCookie.setPath("/");
            jwtCookie.setMaxAge(24 * 60 * 60);
            httpServletResponse.addCookie(jwtCookie);
            UserDto userDto = new UserDto();
            userDto.setEmail(email);
            userDto.setMessage("login success");
            userDto.setUserId(user.getUserId());
            userDto.setName(user.getName());
            return ResponseEntity.status(HttpStatus.CREATED).body(userDto);
        }
        else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new UserDto("Incorrect password"));
        }
    }
    public String getTokenFromCookie(HttpServletRequest request, String tokenName) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (tokenName.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
    public ResponseEntity<UserDto> validateToken(HttpServletRequest httpServletRequest){
            String token = getTokenFromCookie(httpServletRequest, "jwt");
            if(token != null){
                String email = jwtUtil.validateTokenAndGetEmail(token);
                if (email == null) {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                            .body(new UserDto("Invalid or expired token"));
                }
                User user = userRepo.findByEmail(email);
                if(user != null && !user.isLogout()){
                    UserDto userDto = new UserDto();
                    userDto.setMessage("success");
                    userDto.setName(user.getName());
                    userDto.setUserId(user.getUserId());
                    userDto.setEmail(user.getEmail());
                    return ResponseEntity.status(HttpStatus.ACCEPTED)
                            .body(userDto);
                }
                else{
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(new UserDto("session expired, try Login"));
                }
            }
            else{
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new UserDto("try login"));
            }
    }

    public void googleLogin(OAuth2User principal, HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (principal == null) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Google login failed");
            return;
        }

        String email = principal.getAttribute("email");

        if (!userRepo.existsByEmail(email)) {
            User user = new User();
            String name = extractNameFromMail(email);
            user.setName(name);
            user.setEmail(email);
            user.setOauth(true);
            user.setOAuthName("google");
            assert email != null;
            user.setUserId(UrlCodeGenerator.generateUserId(email));
            userRepo.save(user);
        }

        User user = userRepo.findByEmail(email);
        user.setLogout(false);
        userRepo.save(user);

        String token = jwtUtil.generateToken(email);
        Cookie jwtCookie = new Cookie("jwt", token);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setSecure(false);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(24 * 60 * 60);

        response.addCookie(jwtCookie);

        // ✅ Redirect to frontend
        response.sendRedirect("http://localhost:5173");
    }

    public void githubLogin(OAuth2User principal,
                                              HttpServletRequest request,
                                              HttpServletResponse response) throws IOException {
        if (principal == null) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Google login failed");
            return;
        }

        String email = principal.getAttribute("login") + "@github.local";

        if (!userRepo.existsByEmail(email)) {
            User user = new User();
            String name = extractNameFromMail(email);
            user.setName(name);
            user.setEmail(email);         // Either real or pseudo// Store GitHub username
            user.setUserId(UrlCodeGenerator.generateUserId(email));
            userRepo.save(user);
        }

        User user = userRepo.findByEmail(email);
        user.setLogout(false);
        userRepo.save(user);
        String token = jwtUtil.generateToken(email);
        Cookie jwtCookie = new Cookie("jwt", token);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setSecure(false);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(24 * 60 * 60);
        response.addCookie(jwtCookie);
        response.sendRedirect("http://localhost:5173");

    }

    public ResponseEntity<UserDto> logout(HttpServletRequest request, HttpServletResponse response){
        String token = getTokenFromCookie(request, "jwt");
        String email = jwtUtil.extractEmail(token);
        User user = userRepo.findByEmail(email);
        if(user != null){
            user.setLogout(true);
            userRepo.save(user);
        }

        // Optionally remove cookie
        Cookie cookie = new Cookie("jwt", token);
        cookie.setMaxAge(0);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
        return ResponseEntity.ok(new UserDto("Logged out successfully"));
    }


    public ResponseEntity<OtpDto> forgotPass(String email, HttpServletResponse response){

        if(userRepo.existsByEmail(email)){

            User user  = userRepo.findByEmail(email);

            if(user.isOauth()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new OtpDto("This email is registered via Google. Please sign in using Google."));
            }

            OtpDto otpDto = otpService.sendCodeUsingMail(email);
            return ResponseEntity.status(HttpStatus.ACCEPTED)
                    .body(otpDto);
      }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new OtpDto("Email not found"));
        }
    }

    public ResponseEntity<String> verifyOtp(String email, String code, HttpServletResponse response){
        OtpEntry otpEntry = otpRepo.findByEmail(email);

        if (otpEntry == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Email not found");
        }

        if (otpEntry.getExpired_at().isBefore(LocalDateTime.now())) {
            return ResponseEntity.status(HttpStatus.GONE).body("OTP expired");
        }

        if (!otpEntry.getOtp().equals(code)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("OTP does not match");
        }

        String token = jwtUtil.generateToken(email);
        Cookie cookie = new Cookie("pass_change", token);
        cookie.setMaxAge(5 * 60);
        cookie.setSecure(false);
        cookie.setHttpOnly(true);
        cookie.setPath("/");

        response.addCookie(cookie);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("OTP verified successfully");
    }

    public ResponseEntity<UserDto> resetPass(String email, String newPass, HttpServletRequest request, HttpServletResponse response) {
        try {
            User user = userRepo.findByEmail(email);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new UserDto("User not found"));
            }

            String token = getTokenFromCookie(request, "pass_change");
            if (token == null || !jwtUtil.validateToken(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new UserDto("Invalid or missing token"));
            }

            user.setPassword(PasswordUtil.encode(newPass));
            user.setLogout(false);
            userRepo.save(user);

            // Issue new JWT token
            String jwtToken = jwtUtil.generateToken(email);
            Cookie jwtCookie = new Cookie("jwt", jwtToken);
            jwtCookie.setPath("/");
            jwtCookie.setSecure(true); // Set to false only in development
            jwtCookie.setHttpOnly(true);
            jwtCookie.setMaxAge(24 * 60 * 60);
            response.addCookie(jwtCookie);

            // Cleanup OTP entry
            otpRepo.deleteByEmail(email);

            UserDto userDto = new UserDto();
            userDto.setEmail(email);
            userDto.setUserId(user.getUserId());
            userDto.setName(user.getName());
            userDto.setMessage("Password changed successfully");

            return ResponseEntity.ok(userDto);

        } catch (Exception e) {
            // log.error("Password reset failed", e); // Add logging
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new UserDto("Something went wrong"));
        }
    }

    public ResponseEntity<ServerMsgDto> guestLogin(String guestId){
        GuestUrlTable guestUrlTable = guestUrlRepo.findByGuestId(guestId);
        if(guestUrlTable == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ServerMsgDto("guest id not found"));
        }
        else{
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ServerMsgDto("guest id found"));
        }

    }

}
