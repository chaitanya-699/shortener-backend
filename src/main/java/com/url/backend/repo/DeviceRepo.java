package com.url.backend.repo;

import com.url.backend.entity.DeviceTable;
import org.hibernate.boot.models.JpaAnnotations;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceRepo extends JpaRepository<DeviceTable, Long> {
}
