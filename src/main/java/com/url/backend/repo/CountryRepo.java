package com.url.backend.repo;


import com.url.backend.entity.CountryTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryRepo extends JpaRepository<CountryTable, Long> {

}
