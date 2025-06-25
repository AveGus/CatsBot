package com.avegus.telegramconnector.bot.sender;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.io.File;
import java.util.concurrent.CompletableFuture;

public interface MessageSender {
    void sendMessage(Long userId, String text);
    void sendMarkup(Long userId, InlineKeyboardMarkup markup, String caption);
}
