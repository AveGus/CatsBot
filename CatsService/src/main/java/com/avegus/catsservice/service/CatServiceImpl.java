package com.avegus.catsservice.service;

import com.avegus.catsservice.model.Cat;
import com.avegus.catsservice.repo.CatRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
@Service
public class CatServiceImpl implements CatService {

    private final CatRepo repository;

    @Override
    public void addCat(String name, String fileId, String creatorUsername, Long creatorId) {
        if (!name.isEmpty() && !fileId.isEmpty()) {
            var cat = Cat.create(name, fileId, creatorUsername, creatorId);
            log.info("Added cat: {}", cat);
            repository.save(cat);
        } else {
            log.error("Empty name/file id");
        }
    }

    @Override
    public List<Cat> listUserCats(Long creatorId) {
        return repository.findAllByCreatorId(creatorId);
    }

    @Override
    public Optional<Cat> getCat(UUID catId) {
        return repository.findById(catId);
    }

    @Override
    public void deleteCat(UUID catId, Long whoRequestedId) {
        repository.findById(catId)
                .ifPresent(cat -> {
                    if (cat.getCreatorId().equals(whoRequestedId)) {
                        repository.deleteById(catId);
                    } else {
                        log.warn("User {} tried to delete cat {} without his ownership!", whoRequestedId, catId);
                    }
                });
    }

    // Some control of race condition, BIGSERIAL may be better and faster
    @Transactional
    @Override
    public void likeCat(UUID catId) {
        repository.findById(catId)
                .ifPresent(cat -> {
                    cat.setLikesCount(cat.getLikesCount() + 1);
                    repository.save(cat);
                });
    }

    // Some control of race condition, BIGSERIAL may be better and faster
    @Override
    public void dislikeCat(UUID catId) {
        repository.findById(catId)
                .ifPresent(cat -> {
                    cat.setDislikesCount(cat.getDislikesCount() + 1);
                    repository.save(cat);
                });
    }
}
