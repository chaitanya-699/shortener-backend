package com.url.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OtpEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;
    private String otp;
    private LocalDateTime created_at;
    private LocalDateTime expired_at;
    @Column(nullable = true)
    private LocalDateTime retryTime;

    @Builder.Default
    private int count = 3;

    @Builder.Default
    private boolean used = false;
}
