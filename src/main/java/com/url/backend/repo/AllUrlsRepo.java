package com.url.backend.repo;

import com.url.backend.entity.AllUrls;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AllUrlsRepo extends JpaRepository<AllUrls,Long> {
    public AllUrls findByUrlCode(String urlCode);
}
