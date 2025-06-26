package com.avegus.telegramconnector.broker;

import com.avegus.commons.model.AddCatRequest;

public interface KafkaProducerService {
    void sendLikeEvent(IdS catId);
    void sendDislikeEvent(String catId);
    void requestRandomCat(Long userId);
    void requestUserCats(Long userId);
    void requestUserCat(Long userId, String catId);
    void sendDeleteCatRequest(Long userId, String catId);
    void sendAddCatRequest(AddCatRequest request);
}
