package com.url.backend.DTO;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OtpDto {
    private String otp;
    private String message;
    private LocalDateTime expireTime;
    private int count;
    private LocalDateTime retry_time;
    public OtpDto(String message){
        this.message = message;
    }
}
