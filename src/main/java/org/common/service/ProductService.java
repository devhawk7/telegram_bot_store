package org.common.service;

import lombok.RequiredArgsConstructor;
import org.common.model.Product;
import org.common.repository.ProductRepository;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class ProductService implements BaseService<Product> {
    private final ProductRepository productRepository;
    private BasketService basketService;

    public void create(Product product)  {
        productRepository.create(product);
    }


    @Override
    public List<Product> list() throws IOException {
        return productRepository.findAll();
    }

    @Override
    public List<Product> getById(UUID id) throws IOException {
        List<Product> list = productRepository.findAll();
        return list.stream()
                .filter(product -> product.getCategoryId().equals(id))
                .toList();
    }

    public Product hasProduct(UUID id) throws IOException {
        for (Product product : productRepository.findAll()) {
            if (product.getId().equals(id)) {
                return product;
            }

        }
        return null;
    }

}
