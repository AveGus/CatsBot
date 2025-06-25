package com.avegus.telegramconnector.bot.sender;

import lombok.SneakyThrows;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class MessageSenderHelper {
    public static SendMessage toSendMessage(Long userId, String text) {
        return SendMessage.builder()
                .chatId(String.valueOf(userId))
                .text(text)
                .build();
    }

    @SneakyThrows
    public static SendPhoto toSendPhoto(Long userId, byte[] photo, String fileName) {
        var inputFile = new InputFile();
        inputFile.setMedia(byteArrayToFile(photo, fileName));

        return SendPhoto.builder()
                .chatId(String.valueOf(userId))
                .photo(inputFile)
                .build();
    }

    public static File byteArrayToFile(byte[] bytes, String fileName) throws IOException {
        File file = new File(fileName);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(bytes);
        }
        return file;
    }

    public static SendMessage toSendMarkupWithCaption(Long userId, InlineKeyboardMarkup markup, String caption) {
        var msg = toSendMessage(userId, caption);
        msg.setReplyMarkup(markup);
        return msg;
    }
}
