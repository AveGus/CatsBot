package com.avegus.catsservice.repo;

import com.avegus.catsservice.model.Cat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CatRepo extends JpaRepository<Cat, UUID> {
}
