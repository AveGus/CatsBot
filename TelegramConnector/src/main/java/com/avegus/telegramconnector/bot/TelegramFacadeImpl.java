package com.avegus.telegramconnector.bot;

import com.avegus.telegramconnector.bot.handler.MessageHandler;
import com.avegus.telegramconnector.bot.sender.MessageSenderHelper;
import com.avegus.telegramconnector.model.dto.CallbackQueryData;
import com.avegus.telegramconnector.service.state.BotStateService;
import com.avegus.telegramconnector.model.enums.BotState;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethodMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Slf4j
public class TelegramFacadeImpl implements TelegramFacade {

    private final BotStateService botStateService;
    private final List<MessageHandler> handlers;
    private final ObjectMapper objectMapper;

    @Override
    public Optional<BotApiMethodMessage> handleUpdate(Update update) {

        var state = getBotState(update);
        for (MessageHandler handler : handlers) {

            if (handler.canHandle(update, state)) {
                handler.handle(update);
                return Optional.empty();
            }
        }

        try {
            Long userId;
            if (update.hasMessage()) {
                userId = update.getMessage().getFrom().getId();
            } else {
                userId = update.getCallbackQuery().getFrom().getId();
            }

            return Optional.of(
                    MessageSenderHelper.toSendMessage(userId, "Meow")
            );
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Optional.empty();
        }
    }

    /**
     * Create if not exists and get bot state for user
     */
    private BotState getBotState(Update update) {

        // Get state form callback
        if (update.hasCallbackQuery()) {
            return tryGetStatusFromCallback(update.getCallbackQuery());
        }

        if (!update.hasMessage()) {
            return BotState.UNKNOWN;
        }

        // Or else from DB
        var text = update.getMessage().getText();
        var userId = update.getMessage().getChatId();
        var username = update.getMessage().getFrom().getUserName();

        var maybeState = botStateService.getCurrentState(userId);

        BotState state;
        if (maybeState.isEmpty() || Objects.equals(text, "/start")) {
            state = BotState.JOINED;
            botStateService.updateState(userId, username, state);
        } else {
            state = maybeState.get();
        }
        return state;
    }

    private BotState tryGetStatusFromCallback(CallbackQuery callbackQuery) {
        try {
            var data = objectMapper.readValue(callbackQuery.getData(), CallbackQueryData.class);
            return data.getState();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return BotState.UNKNOWN;
        }
    }
}
