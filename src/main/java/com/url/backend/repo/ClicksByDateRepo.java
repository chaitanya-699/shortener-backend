package com.url.backend.repo;

import com.url.backend.entity.ClicksByDate;
import com.url.backend.entity.UserUrlEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClicksByDateRepo extends JpaRepository<ClicksByDate, Long> {
    List<ClicksByDate> findByUrlEntry(UserUrlEntry entry);

    @Query("SELECT c FROM ClicksByDate c WHERE c.urlEntry = :entry ORDER BY c.clicks DESC")
    List<ClicksByDate> findTopByUrlEntryOrderByClicksDesc(@Param("entry") UserUrlEntry entry);

    @Query(value = """
    SELECT * FROM clicks_by_date 
    WHERE user_url_entry = :entryId 
    ORDER BY clicks DESC 
    LIMIT 1
""", nativeQuery = true)
    ClicksByDate findPeakClickDay(@Param("entryId") Long entryId);
}
