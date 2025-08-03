package com.url.backend.repo;

import com.url.backend.entity.OtpEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OtpRepo extends JpaRepository<OtpEntry, Long> {

    boolean existsByEmail(String email);

    OtpEntry findByEmail(String email);

    void deleteByEmail(String email);
}   
