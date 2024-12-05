package org.bot.strategy;

import org.bot.model.enums.State;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;

public abstract class TelegramBotStrategy {
    public abstract State state();
    public abstract SendMessage execute(Update update) throws IOException;
    public abstract EditMessageReplyMarkup execute(CallbackQuery update) throws IOException, TelegramApiException;

    protected SendMessage executeJob(Long chatId, String text, ReplyKeyboard r) {
        SendMessage sendMessage = new SendMessage(chatId.toString(), text);
        sendMessage.setReplyMarkup(r);

        return sendMessage;
    }

    protected EditMessageReplyMarkup executeJob(Long chatId, InlineKeyboardMarkup r, int messageId) throws TelegramApiException {
        EditMessageReplyMarkup editMessageReplyMarkup = new EditMessageReplyMarkup();
        editMessageReplyMarkup.setReplyMarkup(r);
        editMessageReplyMarkup.setMessageId(messageId);
        editMessageReplyMarkup.setChatId(chatId);
        return editMessageReplyMarkup;
    }
}
