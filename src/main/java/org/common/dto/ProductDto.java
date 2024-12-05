package org.common.dto;

import org.common.model.Product;

import java.util.ArrayList;
import java.util.List;

public record ProductDto(String name, double price) {
    public static ProductDto from(Product product) {
        return new ProductDto(product.getName(), product.getPrice());
    }

    public static ArrayList<ProductDto> from(List<Product> products) {
        ArrayList<ProductDto> dtos = new ArrayList<>();
        for (Product product : products) {
            ProductDto dto = from(product);
            dtos.add(dto);
        }
        return dtos;
    }

    @Override
    public String toString() {
        return "Name : " + name + '\'' + ", Price : " + price + "\n";
    }
}
