package com.avegus.telegramconnector.bot;

import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethodMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;

/**
 * Обрабатывает события от телеги, распределяет по хендлерам
 */
public interface TelegramFacade {

    /**
     * Ответ может быть, а может и не быть (будет отправлен позже). Зависит от хендлера.
     */
    Optional<BotApiMethodMessage> handleUpdate(Update update);
}
