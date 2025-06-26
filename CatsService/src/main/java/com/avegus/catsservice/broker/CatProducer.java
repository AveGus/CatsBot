package com.avegus.catsservice.broker;

import com.avegus.commons.model.CatWithUserId;
import com.avegus.commons.model.CatsWithUserId;

public interface CatProducer {
    void produceRandomCat(CatWithUserId catWithUserId);
    void produceUserCats(CatsWithUserId catsWithUserId);
    void produceUserCat(CatWithUserId catWithUserId);
}
