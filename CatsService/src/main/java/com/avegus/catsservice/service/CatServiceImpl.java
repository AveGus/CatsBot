package com.avegus.catsservice.service;

import com.avegus.catsservice.model.Cat;
import com.avegus.catsservice.model.id.CatId2UserId;
import com.avegus.catsservice.repo.CatRepo;
import com.avegus.catsservice.repo.CatViewRepo;
import com.avegus.commons.model.CatIdWithUserId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@RequiredArgsConstructor
@Slf4j
@Service
public class CatServiceImpl implements CatService {

    private final CatRepo catRepo;
    private final CatViewRepo catViewRepo;

    @Override
    public void addCat(String name, String fileId, String creatorUsername, Long creatorId) {
        if (!name.isEmpty() && !fileId.isEmpty()) {
            var cat = Cat.create(name, fileId, creatorUsername, creatorId);
            log.info("Added cat: {}", cat);
            catRepo.save(cat);
        } else {
            log.error("Empty name/file id");
        }
    }

    @Override
    public List<Cat> listUserCats(Long creatorId) {
        return catRepo.findAllByCreatorId(creatorId);
    }

    @Override
    public Optional<Cat> getCat(UUID catId) {
        return catRepo.findById(catId);
    }

    @Override
    public void deleteCat(UUID catId, Long whoRequestedId) {
        catRepo.findById(catId)
                .ifPresent(cat -> {
                    if (cat.getCreatorId().equals(whoRequestedId)) {
                        catRepo.deleteById(catId);
                    } else {
                        log.warn("User {} tried to delete cat {} without his ownership!", whoRequestedId, catId);
                    }
                });
    }

    @Override
    public void likeCat(CatIdWithUserId catIdWithUserId) {
        var maybeViewInfo = catViewRepo.findById(CatId2UserId.from(catIdWithUserId));

        var catId = UUID.fromString(catIdWithUserId.getCatId());
        catRepo.findById(catId)
                .ifPresent(cat -> {
                    cat.setLikesCount(cat.getLikesCount() + 1);
                    catRepo.save(cat);

                    maybeViewInfo.ifPresent(viewInfo -> {
                        viewInfo.setIsLike(true);
                        viewInfo.setViewedAt(LocalDateTime.now());
                        viewInfo.setViewedTimes(viewInfo.getViewedTimes() + 1);
                        catViewRepo.save(viewInfo);
                    });
                });
    }

    @Override
    public void dislikeCat(CatIdWithUserId catIdWithUserId) {
        var maybeViewInfo = catViewRepo.findById(CatId2UserId.from(catIdWithUserId));

        var catId = UUID.fromString(catIdWithUserId.getCatId());
        catRepo.findById(catId)
                .ifPresent(cat -> {
                    cat.setDislikesCount(cat.getDislikesCount() + 1);
                    catRepo.save(cat);

                    maybeViewInfo.ifPresent(viewInfo -> {
                        viewInfo.setIsLike(false);
                        viewInfo.setViewedAt(LocalDateTime.now());
                        viewInfo.setViewedTimes(viewInfo.getViewedTimes() + 1);
                        catViewRepo.save(viewInfo);
                    });
                });
    }

    @Override
    public Optional<Cat> randomCatFor(Long whoRequested) {
        // Empty viewedCatsIds on findAllByCreatorIdNotAndIdNotIn gives always empty resultSet .-.
        var cats = catRepo.findAllByCreatorIdNot(whoRequested)
                .stream()
                .sorted(Comparator.comparing(Cat::getCreated).reversed())
                .toList();
        log.info("Found {} possible random cats for {}", cats.size(), whoRequested);

        return cats.stream().findFirst();
    }
}
