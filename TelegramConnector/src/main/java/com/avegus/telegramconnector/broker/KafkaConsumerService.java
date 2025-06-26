package com.avegus.telegramconnector.broker;


public interface KafkaConsumerService {
    void consumeRandomCat(CatWithUserId catWithUserId);
    void consumeUserCats(CatsWithUserId catsWithUserId);
    void consumeUserCat(CatWithUserId catWithUserId);
}
