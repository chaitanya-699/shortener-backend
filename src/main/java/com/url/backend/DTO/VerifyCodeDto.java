package com.url.backend.DTO;


import jakarta.persistence.AttributeOverride;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VerifyCodeDto {
    private String email;
    private String code;
}
