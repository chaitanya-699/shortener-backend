package com.url.backend.repo;


import com.url.backend.entity.UserUrlTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserUrlTableRepo extends JpaRepository<UserUrlTable, Long> {

    UserUrlTable findByUserId(String userId);

}
