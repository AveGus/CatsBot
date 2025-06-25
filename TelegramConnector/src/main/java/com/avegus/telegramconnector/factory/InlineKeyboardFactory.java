package com.avegus.telegramconnector.factory;

import com.avegus.telegramconnector.model.dto.CallbackQueryData;
import com.avegus.telegramconnector.model.dto.CatIdWithNameDto;
import com.avegus.telegramconnector.model.enums.BotState;
import com.avegus.telegramconnector.model.enums.Rating;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InlineKeyboardFactory {

    private static final Logger log =  java.util.logging.Logger.getLogger(InlineKeyboardFactory.class.getName());
    private static final ObjectMapper om = new ObjectMapper();


    @SneakyThrows
    public static InlineKeyboardMarkup catRatingMarkup(String catId) {
        var row1 = new InlineKeyboardRow();
        var row2 = new InlineKeyboardRow();

        row1.add(InlineKeyboardButton.builder()
                .text("Круто!")
                .callbackData(
                        om.writeValueAsString(
                                new CallbackQueryData(BotState.RATE_CAT, catId + ":" + Rating.LIKE.name())
                        )
                )
                .build());
        row1.add(InlineKeyboardButton.builder()
                .text("Не круто")
                .callbackData(
                        om.writeValueAsString(
                                new CallbackQueryData(BotState.RATE_CAT, catId + ":" + Rating.DISLIKE.name())
                        )
                )
                .build());


        row2.add(InlineKeyboardButton.builder()
                .text("Назад")
                .callbackData(
                        om.writeValueAsString(
                                new CallbackQueryData(BotState.MAIN_MENU, null)
                        )
                )
                .build());

        return InlineKeyboardMarkup.builder()
                .keyboard(List.of(row1, row2))
                .build();
    }

    @SneakyThrows
    public static InlineKeyboardMarkup menuMarkup() {
        var row1 = new InlineKeyboardRow();
        row1.add(InlineKeyboardButton.builder()
                .text("Мои котики")
                .callbackData(
                        om.writeValueAsString(
                                new CallbackQueryData(BotState.MY_CATS, null)
                        )
                )
                .build());
        row1.add(InlineKeyboardButton.builder()
                .text("Смотреть котиков")
                .callbackData(
                        om.writeValueAsString(
                                new CallbackQueryData(BotState.LIST_ALL_CATS, null)
                        )
                )
                .build());
        row1.add(InlineKeyboardButton.builder()
                .text("Добавить котика")
                .callbackData(
                        om.writeValueAsString(
                                new CallbackQueryData(BotState.ADD_CAT, null)
                        )
                )
                .build());

        return InlineKeyboardMarkup.builder()
                .keyboard(List.of(row1))
                .build();
    }

    @SneakyThrows
    public static InlineKeyboardMarkup myCatCardMarkup(String catId) {
        var row1 = new InlineKeyboardRow();
        var row2 = new InlineKeyboardRow();

        row1.add(InlineKeyboardButton.builder()
                .text("Удалить")
                .callbackData(
                        om.writeValueAsString(
                                new CallbackQueryData(BotState.DELETE, catId)
                        )
                )
                .build());

        row2.add(InlineKeyboardButton.builder()
                .text("Назад")
                .callbackData(
                        om.writeValueAsString(
                                new CallbackQueryData(BotState.MAIN_MENU, null)
                        )
                )
                .build());

        return InlineKeyboardMarkup.builder()
                .keyboard(List.of(row1, row2))
                .build();
    }

    @SneakyThrows
    public static InlineKeyboardMarkup myCatsMarkup(List<CatIdWithNameDto> catDtos) {
        var row1 = new InlineKeyboardRow();
        var row2 = new InlineKeyboardRow();

        catDtos.forEach(catDto -> {
            try {
                row1.add(InlineKeyboardButton.builder()
                        .text(catDto.getName())
                        .callbackData(
                                om.writeValueAsString(
                                        new CallbackQueryData(BotState.MY_CAT_ACTIONS, catDto.getId())
                                )
                        )
                        .build());
            } catch (JsonProcessingException e) {
                var msg = "Unable to create my cat card of " + catDto.toString() + " " + e.getMessage();
                log.log(Level.WARNING, msg);
            }
        });

        row2.add(InlineKeyboardButton.builder()
                .text("Назад")
                .callbackData(
                        om.writeValueAsString(
                                new CallbackQueryData(BotState.MAIN_MENU, null)
                        )
                )
                .build());

        return InlineKeyboardMarkup.builder()
                .keyboard(List.of(row1, row2))
                .build();
    }
}
