package org.admin;



import org.common.config.DataSourceConnection;
import org.common.model.Category;
import org.common.model.Product;
import org.common.repository.CategoryRepository;
import org.common.repository.ProductRepository;
import org.common.service.CategoryService;
import org.common.service.ProductService;

import java.io.IOException;
import java.util.Scanner;
import java.util.UUID;
public class Main {
    private static final DataSourceConnection dataSourceConnection = new DataSourceConnection();

    public static void main(String[] args) throws IOException {
        Scanner scannerStr = new Scanner(System.in);
        Scanner scannerInt = new Scanner(System.in);
        CategoryRepository categoryRepository = new CategoryRepository(dataSourceConnection);
        CategoryService categoryService = new CategoryService(categoryRepository);
        ProductRepository productRepository = new ProductRepository(dataSourceConnection);
        ProductService productService = new ProductService(productRepository);

        int stepCode = 10;
        while (stepCode != 0) {

            System.out.println("1. Add Category 2. Add Product");
            stepCode = scannerInt.nextInt();

            switch (stepCode) {
                case 1 -> {
                    Category category = new Category();
                    System.out.println("enter category name: ");
                    category.setName(scannerStr.nextLine());
                    System.out.println("parent id?");
                    String s = scannerStr.nextLine();
                    category.setParentId(s.length() > 1 ? UUID.fromString(s) : null);
                    categoryService.add(category);
                }

                case 2 -> {
                    Product product = new Product();
                    System.out.println("enter product name");
                    product.setName(scannerStr.nextLine());

                    System.out.println("enter price");
                    product.setPrice(scannerInt.nextDouble());

                    System.out.println("enter CategoryId");
                    product.setCategoryId(UUID.fromString(scannerStr.nextLine()));
                    System.out.println("Enter URL of product : ");
                    product.setProductPhoto(scannerStr.nextLine());
                    productService.create(product);
                }
            }
        }
    }
}
