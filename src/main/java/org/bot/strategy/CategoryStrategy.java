package org.bot.strategy;

import lombok.RequiredArgsConstructor;
import org.bot.model.enums.State;
import org.bot.service.InlineMarkupService;
import org.bot.service.UserService;
import org.common.model.Category;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.IOException;

@RequiredArgsConstructor
public class CategoryStrategy extends TelegramBotStrategy {

    private final UserService userService;
    private final InlineMarkupService<Category> categoryInlineMarkupService;

    @Override
    public State state() {
        return State.CATEGORY;
    }

    @Override
    public SendMessage execute(Update update) throws IOException {
        Long chatId = update.getMessage().getChatId();
        userService.setState(chatId, State.CATEGORY);
        return executeJob(chatId, "choose category",
                categoryInlineMarkupService.mainInlineKeyboardMarkup(3));
    }

    @Override
    public EditMessageReplyMarkup execute(CallbackQuery update) throws IOException {
        return null;
    }
}
