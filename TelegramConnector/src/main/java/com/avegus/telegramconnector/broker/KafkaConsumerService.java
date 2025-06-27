package com.avegus.telegramconnector.broker;


import com.avegus.commons.model.CatWithUserId;
import com.avegus.commons.model.CatsWithUserId;

public interface KafkaConsumerService {
    void consumeRandomCat(CatWithUserId catWithUserId);
    void consumeUserCats(CatsWithUserId catsWithUserId);
    void consumeUserCat(CatWithUserId catWithUserId);
}
