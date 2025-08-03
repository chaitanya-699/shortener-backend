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
public class DeviceTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String device;
    private int clicks;
    private int percentage;

    @ManyToOne
    @JoinColumn(name = "user_url_entry")
    @JsonBackReference
    private UserUrlEntry urlEntry;

}
