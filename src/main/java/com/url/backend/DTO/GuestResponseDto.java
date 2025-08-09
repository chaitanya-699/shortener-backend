package com.url.backend.DTO;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class GuestResponseDto {

    private String guestId;
    private String originalUrl;
    private String shortUrl;
    private String urlCode;
    private String message;
    private String description;
    private Long id;
    private LocalDateTime createdAt;

    public GuestResponseDto(String message){
        this.message = message;
    }

}
