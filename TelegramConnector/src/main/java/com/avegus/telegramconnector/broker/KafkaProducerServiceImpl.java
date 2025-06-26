package com.avegus.telegramconnector.broker;

import com.avegus.telegramconnector.broker.dto.AddCatRequest;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerServiceImpl implements KafkaProducerService {

    @Override
    public void sendLikeEvent(String catId) {

    }

    @Override
    public void sendDislikeEvent(String catId) {

    }

    @Override
    public void requestRandomCat(Long userId) {

    }

    @Override
    public void requestUserCats(Long userId) {

    }

    @Override
    public void requestUserCat(Long userId, String catId) {

    }

    @Override
    public void sendDeleteCatRequest(Long userId, String catId) {

    }

    @Override
    public void sendAddCatRequest(AddCatRequest request) {

    }
}
