package com.avegus.telegramconnector.bot.handler.randomcats;

import com.avegus.telegramconnector.bot.handler.MessageHandler;
import com.avegus.telegramconnector.bot.sender.MessageSender;
import com.avegus.telegramconnector.broker.KafkaProducerService;
import com.avegus.telegramconnector.broker.dto.UpdateData;
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

    @SneakyThrows
    public void handle(UpdateData update) {

        String catId;
        Rating catRate;
        try {
            var payload = update.getCallbackData().get().getPayload();
            catId = payload.split(":")[0];
            catRate = Rating.valueOf(payload.split(":")[1]);

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
                kafkaProducer.sendLikeEvent(catId);
                break;
            case DISLIKE:
                kafkaProducer.sendDislikeEvent(catId);
                break;
            default:
                log.warn("Received a callback query with invalid rating");
                return;
        }

        kafkaProducer.requestUserCats(update.getUserId());
        // TODO should be sent by consumer
        messageSender.sendMarkup(
                update.getUserId(),
                InlineKeyboardFactory.catRatingMarkup("fakeId"),
                String.format(Captions.FAKE_CAT, 10, 1));
    }

    public boolean canHandle(UpdateData update) {
        return update.hasCallbackData() && update.getBotState() == BotState.RATE_CAT;
    }
}
