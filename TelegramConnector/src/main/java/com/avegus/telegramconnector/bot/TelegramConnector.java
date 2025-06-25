package com.avegus.telegramconnector.bot;

import com.avegus.telegramconnector.bot.handler.TelegramFacade;
import lombok.RequiredArgsConstructor;
import org.jvnet.hk2.annotations.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
@RequiredArgsConstructor
public class TelegramConnector extends TelegramLongPollingBot {

    private final TelegramFacade telegramFacade;

    @Override
    public void onUpdateReceived(Update update) {
        telegramFacade.handleUpdate(update);
    }

    @Override
    public String getBotUsername() {
        return "Catsss!";
    }
}
