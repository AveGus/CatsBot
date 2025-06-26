package com.avegus.telegramconnector.bot.handler.mycats;

import com.avegus.telegramconnector.bot.handler.MessageHandler;
import com.avegus.telegramconnector.bot.sender.MessageSender;
import com.avegus.telegramconnector.broker.KafkaProducerService;
import com.avegus.telegramconnector.model.dto.UpdateData;
import com.avegus.telegramconnector.factory.InlineKeyboardFactory;
import com.avegus.telegramconnector.model.enums.BotState;
import com.avegus.telegramconnector.model.enums.Captions;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class MyCatsActionsHandler implements MessageHandler {
    private final KafkaProducerService kafkaProducer;
    private final MessageSender messageSender;

    @SneakyThrows
    public void handle(UpdateData update) {
        var catId = update.getCallbackData().get().getPayload();

        if (catId != null && !catId.isEmpty()) {
            kafkaProducer.requestUserCat(update.getUserId(), catId);
            // TODO should be sent by consumer
            messageSender.sendMarkup(update.getUserId(), InlineKeyboardFactory.myCatCardMarkup(catId), String.format(Captions.FAKE_CAT, 10, 1));
        } else {
            log.error("Invalid callback query with cat id");
        }
    }

    public boolean canHandle(UpdateData update) {
        return update.hasCallbackData() && update.getBotState() == BotState.MY_CAT_ACTIONS;
    }
}
