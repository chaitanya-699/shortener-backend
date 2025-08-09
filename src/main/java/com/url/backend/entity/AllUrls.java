package com.url.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AllUrls {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long Id;

    @Column(name = "url_code", unique = true, nullable = false)
    private String urlCode;

    @Column(name = "short_url", nullable = true)
    private String shortUrl;

    @Column(name = "original_url", nullable = false)
    private String originalUrl;

    private String userId;

    @Builder.Default
    private boolean isGuest = false;

    @Builder.Default
    private boolean isUser = false;

    private String guestId;

    @Builder.Default
    private int clicks = 0;

    @Builder.Default
    private boolean isBlocked = false;

    private LocalDateTime createdAt;
}
