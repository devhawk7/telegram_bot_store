package org.bot.service;

import lombok.RequiredArgsConstructor;
import org.common.model.Category;
import org.common.model.Product;
import org.common.service.BaseService;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class MarkupService<T> {
    private final BaseService<T> baseService;

    public InlineKeyboardMarkup inlineKeyboardMarkup(int col) throws IOException {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> in = new ArrayList<>();
        inlineKeyboardMarkup.setKeyboard(in);
        List<T> list = baseService.list();

        List<InlineKeyboardButton> row = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (i % col == 0){
                in.add(row);
                row = new ArrayList<>();
            }
            row.add(inlineKeyboardButton(list.get(i)));
        }

        if (!row.isEmpty()) {
            in.add(row);
        }

        return inlineKeyboardMarkup;
    }

    private InlineKeyboardButton inlineKeyboardButton(T t){
        InlineKeyboardButton button = new InlineKeyboardButton();
        if (t instanceof Product product) {
            button.setText(product.getName());
            button.setCallbackData(product.getId().toString());
        }else if (t instanceof Category category) {
            button.setText(category.getName());
            button.setCallbackData(category.getId().toString());
        }
        return button;
    }
}
