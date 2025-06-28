package com.avegus.catsservice.service;

import com.avegus.catsservice.model.Cat;
import com.avegus.catsservice.model.CatView;
import com.avegus.catsservice.model.id.CatId2UserId;
import com.avegus.catsservice.repo.CatRepo;
import com.avegus.catsservice.repo.CatViewRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
@Service
public class CatServiceImpl implements CatService {

    private final CatRepo catRepo;
    private final CatViewRepo catViewRepo;

    private final Random rand = new Random();

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

    // Some control of race condition, BIGSERIAL may be better and faster
    @Transactional
    @Override
    public void likeCat(UUID catId) {
        catRepo.findById(catId)
                .ifPresent(cat -> {
                    cat.setLikesCount(cat.getLikesCount() + 1);
                    catRepo.save(cat);
                });
    }

    // Some control of race condition, BIGSERIAL may be better and faster
    @Override
    public void dislikeCat(UUID catId) {
        catRepo.findById(catId)
                .ifPresent(cat -> {
                    cat.setDislikesCount(cat.getDislikesCount() + 1);
                    catRepo.save(cat);
                });
    }

    @Transactional
    @Override
    public Optional<Cat> randomCatFor(Long whoRequested) {
        var viewedCatsIds = catViewRepo.findAllById_UserId(whoRequested)
                .stream()
                .map(CatView::getId)
                .map(CatId2UserId::getCatId)
                .toList();

        // Empty viewedCatsIds on findAllByCreatorIdNotAndIdNotIn gives always empty resultSet .-.
        List<Cat> cats;
        if (!viewedCatsIds.isEmpty()) {
            cats = catRepo.findAllByCreatorIdNotAndIdNotIn(whoRequested, viewedCatsIds);
        } else {
            cats = catRepo.findAllByCreatorIdNot(whoRequested);
        }
        log.info("Found {} possible random cats for {}, viewed cats: {}", cats.size(), whoRequested, viewedCatsIds.size());

        // Do stuff with found or return Optional of empty
        return cats.stream().findFirst().map(cat -> {
            var cat2Send = cats.get(rand.nextInt(cats.size()));
            var catViewData = new CatView(whoRequested, cat2Send.getId());
            catViewRepo.save(catViewData);
            return cat;
        });
    }
}
