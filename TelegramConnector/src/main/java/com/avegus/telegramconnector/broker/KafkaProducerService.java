package com.avegus.telegramconnector.broker;

import com.avegus.telegramconnector.broker.dto.AddCatRequest;

public interface KafkaProducerService {
    void sendLikeEvent(String userId, String catId);
    void sendDislikeEvent(String userId, String catId);
    void requestRandomCat(String userId);
    void requestUserCats(String userId);
    void requestUserCat(String userId, String catId);
    void sendDeleteCatRequest(String userId, String catId);
    void sendAddCatRequest(AddCatRequest request);
}
