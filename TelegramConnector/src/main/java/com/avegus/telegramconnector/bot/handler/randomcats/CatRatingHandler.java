package com.avegus.telegramconnector.bot.handler.randomcats;

import com.avegus.telegramconnector.bot.handler.MessageHandler;
import com.avegus.telegramconnector.bot.sender.MessageSender;
import com.avegus.telegramconnector.broker.KafkaProducerService;
import com.avegus.telegramconnector.factory.InlineKeyboardFactory;
import com.avegus.telegramconnector.model.dto.CallbackQueryData;
import com.avegus.telegramconnector.model.enums.BotState;
import com.avegus.telegramconnector.model.enums.Captions;
import com.avegus.telegramconnector.model.enums.Rating;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
@RequiredArgsConstructor
@Service
public class CatRatingHandler implements MessageHandler {
    private final KafkaProducerService kafkaProducer;
    private final MessageSender messageSender;
    private final ObjectMapper om;

    @SneakyThrows
    public void handle(Update update) {
        var userId = update.getCallbackQuery().getFrom().getId();
        var data = om.readValue(update.getCallbackQuery().getData(), CallbackQueryData.class);

        String catId;
        Rating catRate;
        try {
            catId = data.getPayload().split(":")[0];
            catRate = Rating.valueOf(data.getPayload().split(":")[1]);

            if (catId == null || catId.isEmpty()) {
                log.error("Empty catId in CatRatingHandler");
                return;
            }
        } catch (Exception e) {
            log.error("Cannot parse data in CatRatingHandler, ", e);
            return;
        }

        switch (catRate) {
            case LIKE:
                this.kafkaProducer.sendLikeEvent(userId.toString(), catId);
                break;
            case DISLIKE:
                this.kafkaProducer.sendDislikeEvent(userId.toString(), catId);
                break;
            default:
                log.warn("Received a callback query with invalid rating");
                return;
        }
        this.messageSender.sendMessage(userId, "Ответ отправлен!");

        this.kafkaProducer.requestUserCats(userId.toString());
        // TODO should be sent by consumer
        this.messageSender.sendMarkup(userId, InlineKeyboardFactory.catRatingMarkup("fakeId"), Captions.FAKE_CAT);
    }

    public boolean canHandle(Update update, BotState state) {
        return update.hasCallbackQuery() && state == BotState.RATE_CAT;
    }
}
