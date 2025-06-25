package com.avegus.telegramconnector.bot.handler.addcat;

import com.avegus.telegramconnector.bot.handler.MessageHandler;
import com.avegus.telegramconnector.bot.sender.MessageSender;
import com.avegus.telegramconnector.broker.KafkaProducerService;
import com.avegus.telegramconnector.broker.dto.AddCatRequest;
import com.avegus.telegramconnector.factory.InlineKeyboardFactory;
import com.avegus.telegramconnector.model.enums.BotState;
import com.avegus.telegramconnector.model.enums.Captions;
import com.avegus.telegramconnector.service.state.BotStateService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

@RequiredArgsConstructor
@Slf4j
@Service
public class AddCatNameHandler implements MessageHandler {

    private final KafkaProducerService kafkaProducerService;
    private final MessageSender messageSender;
    private final BotStateService botStateService;
    private final InMemStorageById storageById;

    public void handle(Update update) {
        var userId = update.getMessage().getFrom().getId();
        var username = update.getMessage().getFrom().getUserName();

        if (!update.getMessage().hasText()) {
            messageSender.sendMarkup(userId, InlineKeyboardFactory.menuMarkup(), Captions.WEIRD_CAT_NAME);
            return;
        }

        var fileId = this.storageById.get(userId);
        if (fileId.isEmpty()) {
            botStateService.updateState(userId, username, BotState.ADD_CAT_ASK_PHOTO);
            messageSender.sendMarkup(userId, InlineKeyboardFactory.menuMarkup(), Captions.WEIRD_PHOTO);
            return;
        }

        var catName = update.getMessage().getText();
        botStateService.updateState(userId, username, BotState.MAIN_MENU);
        messageSender.sendMarkup(userId, InlineKeyboardFactory.menuMarkup(), String.format(Captions.CAT_SAVED, catName));

        var addCatRequest = new AddCatRequest(userId, catName, fileId.get());
        kafkaProducerService.sendAddCatRequest(addCatRequest);
    }

    public boolean canHandle(Update update, BotState state) {
        return update.hasMessage() && state == BotState.ADD_CAT_ASK_NAME;
    }
}
