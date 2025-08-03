package com.url.backend.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class ClientDto {
    private String browser;
    private String browserVersion;
    private String os;
    private String device;
    private String ip;
    private String referrer;
    private String userAgent;
}

