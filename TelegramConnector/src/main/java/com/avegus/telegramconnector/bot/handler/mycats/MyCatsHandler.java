package com.avegus.telegramconnector.bot.handler.mycats;

import com.avegus.telegramconnector.bot.handler.MessageHandler;
import com.avegus.telegramconnector.bot.sender.MessageSender;
import com.avegus.telegramconnector.broker.KafkaProducerService;
import com.avegus.telegramconnector.model.dto.UpdateData;
import com.avegus.telegramconnector.factory.InlineKeyboardFactory;
import com.avegus.telegramconnector.model.dto.CatIdWithNameDto;
import com.avegus.telegramconnector.model.enums.BotState;

import java.util.List;

import com.avegus.telegramconnector.model.enums.Captions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class MyCatsHandler implements MessageHandler {
    private final KafkaProducerService kafkaProducer;
    private final MessageSender messageSender;

    public void handle(UpdateData update) {

        kafkaProducer.requestUserCats(update.getUserId());

        // TODO should be sent by consumer
        var cats = List.of(new CatIdWithNameDto("cat1", "Сосиска"), new CatIdWithNameDto("cat2", "Трактор"));
        messageSender.sendMarkup(update.getUserId(), InlineKeyboardFactory.myCatsMarkup(cats), Captions.MY_CATS);
    }

    public boolean canHandle(UpdateData update) {
        return update.hasCallbackData() && update.getBotState() == BotState.MY_CATS;
    }
}
