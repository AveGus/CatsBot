package com.avegus.telegramconnector.broker;

import com.avegus.telegramconnector.broker.dto.AddCatRequest;

public interface KafkaProducerService {
    void sendLikeEvent(String userId, String catId);
    void requestCatList(String userId);
    void requestUserCats(String userId);
    void sendDeleteCatRequest(String userId, String catId);
    void sendAddCatRequest(AddCatRequest request);
}
