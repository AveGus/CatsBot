package com.avegus.telegramconnector.bot.handler;

import com.avegus.telegramconnector.model.enums.BotState;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface MessageHandler {
    void handle(Update update);
    boolean canHandle(Update update, BotState state);
}
