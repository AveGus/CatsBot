package com.avegus.telegramconnector.bot.handler;

import com.avegus.telegramconnector.bot.sender.MessageSender;
import com.avegus.telegramconnector.factory.InlineKeyboardFactory;
import com.avegus.telegramconnector.model.enums.BotState;
import com.avegus.telegramconnector.model.enums.Captions;
import com.avegus.telegramconnector.service.state.BotStateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
@Slf4j
@RequiredArgsConstructor
public class AskNameHandler implements MessageHandler {

    private final BotStateService botStateService;
    private final MessageSender messageSender;

    public void handle(Update update) {
        var text = update.getMessage().getText();
        var userId = update.getMessage().getChatId();
        var username = update.getMessage().getFrom().getUserName();

        this.botStateService.updateState(userId, username, BotState.MAIN_MENU);
        this.messageSender.sendMessage(userId, String.format("Привет, %s!", text));
        this.messageSender.sendMarkup(userId, InlineKeyboardFactory.menuMarkup(), Captions.MENU_CAPTION);
    }

    public boolean canHandle(Update update, BotState state) {
        return update.hasMessage() && state == BotState.ASK_NAME;
    }
}
