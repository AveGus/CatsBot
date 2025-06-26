package com.avegus.catsservice.broker;

import com.avegus.commons.model.CatWithUserId;
import com.avegus.commons.model.CatsWithUserId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CatProducerImpl implements CatProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${topics.cat.response-random}")
    private String randomCatTopic;

    @Value("${topics.cat.response-by-user}")
    private String userAllCatsTopic;

    @Value("${topics.cat.response-by-id}")
    private String userCatByIdTopic;

    @Override
    public void produceRandomCat(CatWithUserId toSend) {
        kafkaTemplate.send(randomCatTopic, toSend);
        log.info("Sent random cat to user {}: {}", toSend.getUserId(), toSend.getCat().getId());
    }

    @Override
    public void produceUserCats(CatsWithUserId toSend) {
        kafkaTemplate.send(userAllCatsTopic, toSend);
        log.info("Sent user cats to user {}: {} cats", toSend.getUserId(), toSend.getCats().size());
    }

    @Override
    public void produceUserCat(CatWithUserId toSend) {
        kafkaTemplate.send(userCatByIdTopic, toSend);
        log.info("Sent single user cat to user {}: {}", toSend.getUserId(), toSend.getCat().getId());
    }
}
