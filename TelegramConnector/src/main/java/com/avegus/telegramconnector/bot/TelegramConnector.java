package com.avegus.telegramconnector.bot;

import com.avegus.telegramconnector.config.BotConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethodMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Service
@RequiredArgsConstructor
@Slf4j
public class TelegramConnector implements SpringLongPollingBot, LongPollingSingleThreadUpdateConsumer {

    private final BotConfig botConfig;
    private final TelegramFacade telegramFacade;
    private final TelegramClient telegramClient;

    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }

    @Override
    public LongPollingUpdateConsumer getUpdatesConsumer() {
        return this;
    }

    @Override
    public void consume(Update update) {
        telegramFacade.handleUpdate(update)
                .map(this::executeSafe);
    }

    private BotApiMethodMessage executeSafe(BotApiMethodMessage message) {
        try {
            telegramClient.execute(message);
        } catch (Exception e) {
            log.error("Failed to send message, ", e);
        }
        return message;
    }
}
