package com.avegus.telegramconnector.bot.handler.addcat;

import com.avegus.telegramconnector.bot.handler.MessageHandler;
import com.avegus.telegramconnector.bot.sender.MessageSender;
import com.avegus.telegramconnector.model.enums.BotState;
import com.avegus.telegramconnector.model.enums.Captions;
import com.avegus.telegramconnector.service.state.BotStateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
@RequiredArgsConstructor
@Service
public class AddCatHandler implements MessageHandler {
    private final MessageSender messageSender;
    private final BotStateService stateService;

    public void handle(Update update) {
        var userId = update.getCallbackQuery().getFrom().getId();
        var username = update.getCallbackQuery().getFrom().getUserName();

        stateService.updateState(userId, username, BotState.ADD_CAT_ASK_PHOTO);
        messageSender.sendMessage(userId, Captions.REQUEST_PHOTO);
    }

    public boolean canHandle(Update update, BotState state) {
        return update.hasCallbackQuery() && state == BotState.ADD_CAT;
    }
}
