package com.url.backend.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.url.backend.DTO.PeakClickDayDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserUrlEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String urlCode;
    private String shortUrl;
    private String originalUrl;
    private String email;
    private String userId;
    private String description;
    private boolean isActive = false;
    private boolean isBlocked = false;
    private String qrData;
    private int totalClicks;
    private LocalDateTime create_at;
    private boolean isGuest = false;

    @OneToMany(mappedBy = "urlEntry", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<CountryTable> countryClicks = new ArrayList<>();

    @OneToMany(mappedBy = "urlEntry", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<BrowserTable> browserTables = new ArrayList<>();

    @OneToMany(mappedBy = "urlEntry", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<DeviceTable> deviceTables = new ArrayList<>();

    @OneToMany(mappedBy = "urlEntry", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<IpTable> ipTables = new ArrayList<>();

    @OneToMany(mappedBy = "urlEntry", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<RecentClicks> recentClicks = new ArrayList<>();

    @OneToMany(mappedBy = "urlEntry", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<ClicksByDate> clicksByDates = new ArrayList<>();

    @OneToMany(mappedBy = "urlEntry", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<TopReferres> topReferrers = new ArrayList<>();

    @OneToMany(mappedBy = "urlEntry", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<OperatingSystems> operatingSystems = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "user_table_id")  // references GuestUrlTable.id
    @JsonBackReference
    private UserUrlTable userUrlTable;

}
