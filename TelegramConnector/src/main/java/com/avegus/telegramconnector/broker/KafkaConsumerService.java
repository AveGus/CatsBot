package com.avegus.telegramconnector.broker;

import com.avegus.telegramconnector.broker.dto.Cat;

import java.util.List;

public interface KafkaConsumerService {
    void consumeCatList(String userId, List<Cat> cats);
    void consumeUserCats(String userId, List<Cat> cats);
    void consumeCatDeleted(String userId, String catId, boolean success);
    void consumeCatAdded(String userId, String catId, boolean success);
    void consumeCatNotFound(String userId, String catId);
}
