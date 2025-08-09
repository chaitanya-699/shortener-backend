package com.url.backend.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class GuestUrlEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "original_url")
    private String originalUrl;

    @Column(name = "url_code", unique = true, nullable = false)
    private String shortCode;

    @Column(name = "short_url", nullable = true)
    private String shortUrl;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "url_des", nullable = true)
    private String description;

    private int clicks = 0;

    @ManyToOne
    @JoinColumn(name = "guest_table_id")  // references GuestUrlTable.id
    @JsonBackReference
    private GuestUrlTable guest;

}
