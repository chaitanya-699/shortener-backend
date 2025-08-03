package com.url.backend.repo;

import com.url.backend.entity.OperatingSystems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OsRepo extends JpaRepository<OperatingSystems, Long> {
}
