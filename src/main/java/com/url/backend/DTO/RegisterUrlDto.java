package com.url.backend.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class RegisterUrlDto {

    private String id;
    private String email;
    private String name;
    private String originalUrl;
    private String description;

}
