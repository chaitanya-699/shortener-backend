package com.url.backend.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CountryTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "country_name", nullable = true)
    private String country;

    @Builder.Default
    private int clicks = 0;

    private double percentage;

    @ManyToOne
    @JoinColumn(name = "user_url_entry")
    @JsonBackReference
    private UserUrlEntry urlEntry;
}
