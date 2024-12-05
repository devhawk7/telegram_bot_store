package org.bot.strategy;

import lombok.RequiredArgsConstructor;
import org.bot.model.enums.State;
import org.bot.service.InlineMarkupService;
import org.bot.service.UserService;
import org.common.model.Category;
import org.common.model.Product;
import org.common.service.CategoryService;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.util.UUID;

@RequiredArgsConstructor
public class ChildCategoryStrategy extends TelegramBotStrategy {

    private final CategoryService categoryService;
    private final InlineMarkupService<Category> categoryInlineMarkupService;
    private final InlineMarkupService<Product> productInlineMarkupService;
    private final UserService userService;

    @Override
    public State state() {
        return State.CHILD_CATEGORY;
    }

    @Override
    public SendMessage execute(Update update) throws IOException {
        return null;
    }

    @Override
    public EditMessageReplyMarkup execute(CallbackQuery callbackQuery) throws IOException, TelegramApiException {
        String categoryId = callbackQuery.getData();
        Long chatId = callbackQuery.getMessage().getChatId();
        boolean hasChildCategory = categoryService.hasChildCategory(UUID.fromString(categoryId));
        Integer messageId = callbackQuery.getMessage().getMessageId();
        if (hasChildCategory) {
            return executeJob(chatId,
                    categoryInlineMarkupService.subInlineKeyboardMarkup(UUID.fromString(categoryId), 3),
                    messageId
            );
        }

        userService.setState(chatId, State.PRODUCT);
        return executeJob(chatId,
                productInlineMarkupService.subInlineKeyboardMarkup(UUID.fromString(categoryId), 4),
                messageId
        );
    }
}
