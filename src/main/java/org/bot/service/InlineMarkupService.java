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
import java.util.UUID;

@RequiredArgsConstructor
public class InlineMarkupService<T> {
    private final BaseService<T> baseService;

    public InlineKeyboardMarkup mainInlineKeyboardMarkup(int col) throws IOException {
        List<T> list = baseService.list();
        return buildInlineKeyboardMarkup(list, col);
    }

    public InlineKeyboardMarkup subInlineKeyboardMarkup(UUID id, int col) throws IOException {
        List<T> list = baseService.getById(id);
        return buildInlineKeyboardMarkup(list, col);
    }

    private InlineKeyboardMarkup buildInlineKeyboardMarkup(List<T> list, int col) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> in = new ArrayList<>();
        inlineKeyboardMarkup.setKeyboard(in);

        List<InlineKeyboardButton> row = new ArrayList<>();
        for (int i = 1; i <= list.size(); i++) {
            row.add(inlineKeyboardButton(list.get(i-1)));
            if (i % col == 0){
                in.add(row);
                row = new ArrayList<>();
            }
        }

        if (!row.isEmpty()) {
            in.add(row);
        }

        return inlineKeyboardMarkup;
    }
    public InlineKeyboardMarkup productInlineKeyboardMarkUp(Product product) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> in = new ArrayList<>();
        inlineKeyboardMarkup.setKeyboard(in);
        List<InlineKeyboardButton> row = new ArrayList<>();
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText("Add to Basket");
        button.setCallbackData("Add to Basket"+product.getId());
        row.add(button);
        in.add(row);
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
