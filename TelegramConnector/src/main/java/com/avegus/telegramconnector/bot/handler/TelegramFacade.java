package com.avegus.telegramconnector.bot.handler;

import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Обрабатывает события от телеги, распределяет по хендлерам
 */
public interface TelegramFacade {
    void handleUpdate(Update update);
}
