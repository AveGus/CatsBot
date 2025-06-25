package com.avegus.telegramconnector.bot.handler.addcat;

import com.avegus.telegramconnector.bot.handler.MessageHandler;
import com.avegus.telegramconnector.bot.sender.MessageSender;
import com.avegus.telegramconnector.broker.KafkaProducerService;
import com.avegus.telegramconnector.factory.InlineKeyboardFactory;
import com.avegus.telegramconnector.model.enums.BotState;
import com.avegus.telegramconnector.model.enums.Captions;
import com.avegus.telegramconnector.service.state.BotStateService;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

@RequiredArgsConstructor
@Slf4j
@Service
public class AddCatPhotoHandler implements MessageHandler, InMemStorageById {

    private final MessageSender messageSender;
    private final BotStateService botStateService;
    private final ConcurrentHashMap<Long, String> userTempCatPhotos = new ConcurrentHashMap<>();

    public void handle(Update update) {
        var userId = update.getMessage().getFrom().getId();
        var username = update.getMessage().getFrom().getUserName();

        if (update.getMessage().hasPhoto()
                && !update.getMessage().getPhoto().isEmpty()
                && update.getMessage().getMediaGroupId() == null) {
            var photos = update.getMessage().getPhoto();

            set(userId, (photos.getLast()).getFileId());

            messageSender.sendMessage(userId, Captions.PHOTO_SAVED_ASK_NAME);
            botStateService.updateState(userId, username, BotState.ADD_CAT_ASK_NAME);
        } else {
            messageSender.sendMarkup(userId, InlineKeyboardFactory.menuMarkup(), Captions.WEIRD_PHOTO);
        }
    }

    public boolean canHandle(Update update, BotState state) {
        return update.hasMessage() && state == BotState.ADD_CAT_ASK_PHOTO;
    }

    public Optional<String> get(Long key) {
        return this.userTempCatPhotos.containsKey(key) ?
                Optional.of(this.userTempCatPhotos.get(key)) : Optional.empty();
    }

    public void set(Long key, String value) {
        this.userTempCatPhotos.put(key, value);
    }
}
