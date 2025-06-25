package com.avegus.telegramconnector.bot.handler.randomcats;

import com.avegus.telegramconnector.bot.handler.MessageHandler;
import com.avegus.telegramconnector.bot.sender.MessageSender;
import com.avegus.telegramconnector.broker.KafkaProducerService;
import com.avegus.telegramconnector.factory.InlineKeyboardFactory;
import com.avegus.telegramconnector.model.enums.BotState;
import com.avegus.telegramconnector.model.enums.Captions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
@RequiredArgsConstructor
@Service
public class ListAllCatsHandler implements MessageHandler {
    private final KafkaProducerService kafkaProducer;
    private final MessageSender messageSender;

    public void handle(Update update) {
        var userId = update.getCallbackQuery().getFrom().getId();

        kafkaProducer.requestUserCats(userId.toString());
        // // TODO should be sent by consumer
        messageSender.sendMarkup(userId, InlineKeyboardFactory.catRatingMarkup("fakeId"), Captions.FAKE_CAT);
    }

    public boolean canHandle(Update update, BotState state) {
        return update.hasCallbackQuery() && state == BotState.LIST_ALL_CATS;
    }
}
