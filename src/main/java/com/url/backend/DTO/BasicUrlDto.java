package com.url.backend.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BasicUrlDto {

    private String userId;
    private String message;
    private String email;
    private String originalUrl;
    private String shortUrl;
    private String urlCode;
    private String qrUrl;
    private LocalDateTime createdAt;

    public BasicUrlDto(String message){
        this.message = message;
    }

}
