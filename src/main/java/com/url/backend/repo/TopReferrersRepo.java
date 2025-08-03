package com.url.backend.repo;

import com.url.backend.entity.TopReferres;
import com.url.backend.entity.UserUrlEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TopReferrersRepo extends JpaRepository<TopReferres, Long> {
    List<TopReferres> findTop3ByUrlEntryOrderByClicksDesc(UserUrlEntry urlEntry);
}
