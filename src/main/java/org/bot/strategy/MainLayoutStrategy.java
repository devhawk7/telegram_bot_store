package org.bot.strategy;

import lombok.RequiredArgsConstructor;
import org.bot.model.enums.State;
import org.bot.service.UserService;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class MainLayoutStrategy extends TelegramBotStrategy {

    private final UserService userService;

    @Override
    public State state() {
        return State.MAIN;
    }

    @Override
    public SendMessage execute(Update update) throws IOException {
        Long chatId = update.getMessage().getChatId();
        userService.setState(chatId, State.MAIN);
        return executeJob(chatId, "Xush kelibsiz!",
                replyKeyboard(List.of("order", "basket", "history"), 2));

    }

    @Override
    public EditMessageReplyMarkup execute(CallbackQuery update) throws IOException, TelegramApiException {
        return null;
    }

    private ReplyKeyboard replyKeyboard(List<String> menus, int col) {
        ReplyKeyboardMarkup replyKeyboard = new ReplyKeyboardMarkup();
        List<KeyboardRow> rows = new ArrayList<>();
        replyKeyboard.setKeyboard(rows);

        KeyboardRow row = new KeyboardRow();
        for (int i = 1; i <= menus.size(); i++) {
            row.add(new KeyboardButton(menus.get(i-1)));
            if (i % col == 0) {
                rows.add(row);
                row = new KeyboardRow();
            }
        }

        if (!row.isEmpty()) {
            rows.add(row);
        }

        return replyKeyboard;
    }
}
