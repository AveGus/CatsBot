package com.avegus.telegramconnector.bot.handler;

import com.avegus.telegramconnector.bot.sender.MessageSender;
import com.avegus.telegramconnector.model.enums.BotState;
import com.avegus.telegramconnector.service.state.BotStateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
@RequiredArgsConstructor
@Service
public class StartHandler implements MessageHandler {
    private final BotStateService botStateService;
    private final MessageSender messageSender;

    public void handle(Update update) {
        var userId = update.getMessage().getChatId();
        var username = update.getMessage().getFrom().getUserName();

        this.botStateService.updateState(userId, username, BotState.ASK_NAME);
        this.messageSender.sendMessage(userId, "Привет! Как тебя зовут?");
    }

    public boolean canHandle(Update update, BotState state) {
        return update.hasMessage() && state == BotState.JOINED;
    }
}
