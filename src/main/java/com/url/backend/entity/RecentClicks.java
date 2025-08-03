package com.url.backend.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
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
public class RecentClicks {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    private String country;
    private String browser;
    private String device;
    private String ip;
    private String referred;
    private String userAgent;

    @ManyToOne
    @JoinColumn(name = "user_url_entry")
    @JsonBackReference
    private UserUrlEntry urlEntry;
}
