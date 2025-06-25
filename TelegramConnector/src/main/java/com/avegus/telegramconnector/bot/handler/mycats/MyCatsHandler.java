package com.avegus.telegramconnector.bot.handler.mycats;

import com.avegus.telegramconnector.bot.handler.MessageHandler;
import com.avegus.telegramconnector.bot.sender.MessageSender;
import com.avegus.telegramconnector.broker.KafkaProducerService;
import com.avegus.telegramconnector.factory.InlineKeyboardFactory;
import com.avegus.telegramconnector.model.dto.CatIdWithNameDto;
import com.avegus.telegramconnector.model.enums.BotState;
import java.util.List;

import com.avegus.telegramconnector.model.enums.Captions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
@RequiredArgsConstructor
@Service
public class MyCatsHandler implements MessageHandler {
    private final KafkaProducerService kafkaProducer;
    private final MessageSender messageSender;

    public void handle(Update update) {
        var userId = update.getCallbackQuery().getFrom().getId();

        kafkaProducer.requestUserCats(userId.toString());

        // TODO should be sent by consumer
        var cats = List.of(new CatIdWithNameDto("cat1", "Сосиска"), new CatIdWithNameDto("cat2", "Трактор"));
        messageSender.sendMarkup(userId, InlineKeyboardFactory.myCatsMarkup(cats), Captions.MY_CATS);
    }

    public boolean canHandle(Update update, BotState state) {
        return update.hasCallbackQuery() && state == BotState.MY_CATS;
    }
}
