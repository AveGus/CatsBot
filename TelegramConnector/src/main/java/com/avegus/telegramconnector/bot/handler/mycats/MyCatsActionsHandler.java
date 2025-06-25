package com.avegus.telegramconnector.bot.handler.mycats;

import com.avegus.telegramconnector.bot.handler.MessageHandler;
import com.avegus.telegramconnector.bot.sender.MessageSender;
import com.avegus.telegramconnector.broker.KafkaProducerService;
import com.avegus.telegramconnector.factory.InlineKeyboardFactory;
import com.avegus.telegramconnector.model.dto.CallbackQueryData;
import com.avegus.telegramconnector.model.enums.BotState;
import com.avegus.telegramconnector.model.enums.Captions;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

@RequiredArgsConstructor
@Slf4j
@Service
public class MyCatsActionsHandler implements MessageHandler {
    private final KafkaProducerService kafkaProducer;
    private final MessageSender messageSender;
    private final ObjectMapper om;

    @SneakyThrows
    public void handle(Update update) {
        var userId = update.getCallbackQuery().getFrom().getId();
        var data = om.readValue(update.getCallbackQuery().getData(), CallbackQueryData.class);
        var catId = data.getPayload();

        if (catId != null && !catId.isEmpty()) {
            kafkaProducer.requestUserCat(userId.toString(), catId);
            // TODO should be sent by consumer
            messageSender.sendMarkup(userId, InlineKeyboardFactory.myCatCardMarkup(catId), String.format(Captions.FAKE_CAT, 10, 1));
        } else {
            log.error("Invalid callback query with cat id");
        }
    }

    public boolean canHandle(Update update, BotState state) {
        return update.hasCallbackQuery() && state == BotState.MY_CAT_ACTIONS;
    }
}
