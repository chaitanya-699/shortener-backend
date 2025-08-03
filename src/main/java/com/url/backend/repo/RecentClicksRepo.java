package com.url.backend.repo;

import com.url.backend.entity.RecentClicks;
import com.url.backend.entity.UserUrlEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface RecentClicksRepo extends JpaRepository<RecentClicks, Long> {

    List<RecentClicks> findByUrlEntry(UserUrlEntry entry);

    long countByUrlEntryAndCreatedAtBetween(UserUrlEntry entry, LocalDateTime start, LocalDateTime end);

    @Query("SELECT COUNT(DISTINCT rc.ip) FROM RecentClicks rc WHERE rc.urlEntry = :entry")
    long countDistinctIpByUrlEntry(@Param("entry") UserUrlEntry entry);

    List<RecentClicks> findTop5ByUrlEntryOrderByCreatedAtDesc(UserUrlEntry urlEntry);
}
