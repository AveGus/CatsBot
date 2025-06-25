package com.avegus.catsservice.service;

import com.avegus.catsservice.model.Cat;

import java.util.Optional;
import java.util.UUID;

public interface CatService {
    void addCat(String name, String fileId, String creatorUsername, Long creatorId);
    Optional<Cat> getCat(UUID catId);
    void deleteCat(UUID catId, Long creatorId);

    void likeCat(UUID catId);
    void dislikeCat(UUID catId);
}
