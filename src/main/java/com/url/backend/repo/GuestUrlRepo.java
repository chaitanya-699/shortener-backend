package com.url.backend.repo;


import com.url.backend.entity.GuestUrlTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GuestUrlRepo extends JpaRepository<GuestUrlTable, Long> {

    public GuestUrlTable findByGuestId(String guestId);
}
