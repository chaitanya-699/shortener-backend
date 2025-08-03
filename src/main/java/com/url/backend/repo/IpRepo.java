package com.url.backend.repo;

import com.url.backend.entity.IpTable;
import org.hibernate.boot.models.JpaAnnotations;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IpRepo extends JpaRepository<IpTable, Long> {
}
