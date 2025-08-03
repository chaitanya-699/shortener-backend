package com.url.backend.repo;

import com.url.backend.entity.BrowserTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BrowserRepo extends JpaRepository<BrowserTable, Long> {
}
