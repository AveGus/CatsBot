package com.avegus.telegramconnector.broker;

import com.avegus.telegramconnector.broker.dto.AddCatRequest;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerServiceImpl implements KafkaProducerService {

    @Override
    public void sendLikeEvent(String userId, String catId) {

    }

    @Override
    public void sendDislikeEvent(String userId, String catId) {

    }

    @Override
    public void requestRandomCat(String userId) {

    }

    @Override
    public void requestUserCats(String userId) {

    }

    @Override
    public void requestUserCat(String userId, String catId) {

    }

    @Override
    public void sendDeleteCatRequest(String userId, String catId) {

    }

    @Override
    public void sendAddCatRequest(AddCatRequest request) {

    }
}
