package com.url.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "hash_pass", nullable = false, length = 255, updatable = true)
    private String password;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "logout_status", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean logout = false;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "is_oauth", nullable = false)
    private boolean isOauth = false;

    @Column(name = "oauth_name", nullable = true)
    private String oAuthName;

    @Column(name = "user_id", unique = true, nullable = false)
    private String userId;

    public User(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }
}
