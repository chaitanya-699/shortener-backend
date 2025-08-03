package com.url.backend.repo;

import com.url.backend.entity.UserUrlEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserUrlEntryRepo extends JpaRepository<UserUrlEntry, Long> {
    UserUrlEntry findByUrlCode(String urlCode);
}
