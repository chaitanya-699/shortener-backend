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
public class TopReferres {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String referred;
    private int clicks=0;

    @ManyToOne
    @JoinColumn(name = "user_url_entry")
    @JsonBackReference
    private UserUrlEntry urlEntry;

}
