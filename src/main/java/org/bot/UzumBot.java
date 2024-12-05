package org.bot;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.bot.service.InlineMarkupService;
import org.common.config.DataSourceConnection;
import org.common.model.Basket;
import org.common.model.Category;
import org.common.model.Product;
import org.common.repository.BasketRepository;
import org.common.repository.CategoryRepository;
import org.common.repository.ProductRepository;
import org.common.service.BasketService;
import org.common.service.CategoryService;
import org.common.service.ProductService;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
public class UzumBot extends TelegramLongPollingBot {
    private static final String USERNAME = "@Basket_apk_mod_bot";
    private static final String BOT_TOKEN = "6772509217:AAHUmX26gQV--8IVOJ3c9iE3KwOX7gV2vJ8";
    private static final DataSourceConnection dataSource = new DataSourceConnection();
    CategoryRepository categoryRepository = new CategoryRepository(dataSource);
    CategoryService categoryService = new CategoryService(categoryRepository);
    InlineMarkupService<Category> inlineMarkupService = new InlineMarkupService<>(categoryService);
    ProductRepository productRepository = new ProductRepository(dataSource);
    ProductService productService = new ProductService(productRepository);
    BasketRepository basketRepository = new BasketRepository(dataSource);
    BasketService basketService = new BasketService(basketRepository,productRepository);
    InlineMarkupService<Product> productInlineMarkupService = new InlineMarkupService<>(productService);

    public UzumBot() {
    }

    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage()) {
            Message message = update.getMessage();
            String text = message.getText();
            try {
                switch (text) {
                    case "/start" -> executeJob(message.getChatId(), "Xush kelibsiz!",
                            replyKeyboard(List.of("Order", "Basket", "History"), 2));

                    case "Order" -> executeJob(message.getChatId(), "Choose Category",
                            inlineMarkupService.mainInlineKeyboardMarkup(3));

                    case "Basket" -> {
                        List<Basket> baskets = basketService.list();
                        String products = basketService.formattedList(baskets, message);
                        executeJob(message.getChatId(), products, null);
                    }
                }
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        } else if (update.hasCallbackQuery()) {
            try {
                CallbackQuery callbackQuery = update.getCallbackQuery();
                String categoryId = callbackQuery.getData();
                for (Product product : productRepository.findAll()) {
                    if (update.getCallbackQuery().getData().equalsIgnoreCase("Add to Basket" + product.getId())) {
                        if(!basketService.hasProduct(update.getCallbackQuery().getMessage().getChatId(),product)) {
                              basketService.saveProduct(update);
                            executeJob(callbackQuery.getMessage().getChatId(),
                                    "Product is successfully added",
                                    null
                            );
                            return;
                        }
                        else {
                            executeJob(callbackQuery.getMessage().getChatId(),
                                    "Product is already added",
                                    null
                            );
                            return;
                        }

                    }
                }
                boolean hasChildCategory = categoryService.hasChildCategory(UUID.fromString(categoryId));
                Product hasProduct = productService.hasProduct(UUID.fromString(categoryId));
                if (hasChildCategory) {
                    executeJob(callbackQuery.getMessage().getChatId(),
                            "Choose  category",
                            inlineMarkupService.subInlineKeyboardMarkup(UUID.fromString(categoryId), 3)
                    );
                } else if (hasProduct != null) {
                    executeJob1(callbackQuery.getMessage().getChatId(),
                            "Name : " + hasProduct.getName() + "\n\nPrice : " + hasProduct.getPrice(),
                            hasProduct.getProductPhoto(),
                            productInlineMarkupService.productInlineKeyboardMarkUp(hasProduct)
                    );

                } else {
                    executeJob(callbackQuery.getMessage().getChatId(),
                            "Choose product",
                            productInlineMarkupService.subInlineKeyboardMarkup(UUID.fromString(categoryId), 4)
                    );
                }
            } catch (Exception e) {
               log.error(e.getMessage());
            }
        }
    }


    @Override
    public String getBotUsername() {
        return USERNAME;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }

    private void executeJob(Long chatId, String text, ReplyKeyboard r) throws TelegramApiException {
        SendMessage sendMessage = new SendMessage(chatId.toString(), text);
        sendMessage.setReplyMarkup(r);
        execute(sendMessage);
    }

    private void executeJob1(Long chatId, String text, String URL, ReplyKeyboard r) throws TelegramApiException, MalformedURLException {
        SendPhoto sendPhoto = new SendPhoto(chatId.toString(), new InputFile(new URL(URL).toString()));
        sendPhoto.setCaption(text);
        sendPhoto.setReplyMarkup(r);
        execute(sendPhoto);
    }

    private ReplyKeyboard replyKeyboard(List<String> menus, int col) {
        ReplyKeyboardMarkup replyKeyboard = new ReplyKeyboardMarkup();
        List<KeyboardRow> rows = new ArrayList<>();
        replyKeyboard.setKeyboard(rows);

        KeyboardRow row = new KeyboardRow();
        for (int i = 1; i <= menus.size(); i++) {
            row.add(new KeyboardButton(menus.get(i - 1)));
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
