package org.common.service;

import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.common.dto.ProductDto;
import org.common.model.Basket;
import org.common.model.Product;
import org.common.repository.BasketRepository;
import org.common.repository.ProductRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BasketService {
    private final BasketRepository basketRepository;
    private final ProductRepository productRepository;

    public BasketService(BasketRepository basketRepository, ProductRepository productRepository) {
        this.basketRepository = basketRepository;
        this.productRepository = productRepository;
    }

    public void create(Basket basket) throws IOException {
        basketRepository.create(basket);
    }

    public Basket getById(Long id) {
        for (Basket basket : basketRepository.findAll()) {
            if (basket.getUserId().equals(id)) {
                return basket;
            }
        }
        return null;
    }


    public double calculateTotalPrice(List<Product> products) {
        double totalPrice = 0;
        for (Product product : products) {
            totalPrice += product.getPrice();
        }
        return totalPrice;
    }


    public boolean getBasket(UUID id) throws IOException {
        if (basketRepository.find(id).getId() == null) {
            return false;
        }
        return true;
    }

    public Basket getBasketByUserId(Long id) throws IOException {
        for (Basket basket : basketRepository.findAll()) {
            if (basket.getUserId().equals(id)) {
                return basket;
            }
        }
        return null;
    }

    public List<Basket> list() throws IOException {
        return basketRepository.findAll();
    }

    public String formattedList(List<Basket> baskets, Message message) throws IOException {
        List<Product> filteredBaskets = new ArrayList<>();
        StringBuilder basketProducts = new StringBuilder("Your basket :\n\n");
        for (Basket basket : baskets) {
            if (basket.getUserId().equals(message.getChatId())) {
                filteredBaskets = productRepository.getProductsByBasketId(basket.getId());
            }
        }
        List<ProductDto> products = ProductDto.from(filteredBaskets);
        products.forEach(product -> basketProducts.append(product.toString()));
        double v = calculateTotalPrice(filteredBaskets);
        basketProducts.append("Total price : " + v + "\n");
        return basketProducts.toString();
    }

    public void saveProduct(Update update) throws IOException {
        for (Product product : productRepository.findAll()) {
            if (update.getCallbackQuery().getData().equalsIgnoreCase("Add to Basket" + product.getId())) {
                Basket basket = getBasketByChatId(update.getCallbackQuery().getMessage().getChatId());
                if (basket == null) {
                    basket = new Basket();
                    basket.setUserId(update.getCallbackQuery().getMessage().getChatId());
                    create(basket);
                }
                basketRepository.addProduct(basket.getId(), product.getId());
                return;
            }
        }
    }

    public boolean hasProduct(Long chatId, Product product) throws IOException {
        Basket basket = getBasketByChatId(chatId);
        if (basket == null) {
            return false;
        }
        List<Product> productList = productRepository.getProductsByBasketId(basket.getId());
            for (Product basketProduct : productList) {
                if (basketProduct.getId().equals(product.getId())) {
                    return true;
                }
            }

        return false;
    }

    private Basket getBasketByChatId(Long chatId) {
        for (Basket basket : basketRepository.findAll()) {
            if (basket.getUserId().equals(chatId)) {
                return basket;
            }
        }
        return null;
    }

}