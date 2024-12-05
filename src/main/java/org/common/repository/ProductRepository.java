package org.common.repository;

import lombok.RequiredArgsConstructor;
import org.common.config.DataSourceConnection;
import org.common.model.Product;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class ProductRepository implements BaseRepository<Product> {
    private final DataSourceConnection dataSourceConnection;

    @Override
    public void create(Product product) {
        String query = "INSERT INTO products (id, name, price, quantity,category_id,photo_link) VALUES (?, ?, ?, ?,?,?)";
        try (Connection connection = dataSourceConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setObject(1, product.getId()); // Generate a random UUID
            statement.setString(2, product.getName());
            statement.setDouble(3, product.getPrice());
            statement.setInt(4, product.getQuantity());
            statement.setObject(5, product.getCategoryId());
            statement.setString(6, product.getProductPhoto());
            statement.executeUpdate();
        } catch (SQLException | IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public UUID update(Product product) {
        String query = "UPDATE products SET name = ?, price = ?, quantity = ? ,photo_link = ? WHERE id = ?";
        try (Connection connection = dataSourceConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, product.getName());
            statement.setDouble(2, product.getPrice());
            statement.setInt(3, product.getQuantity());
            statement.setObject(4, product.getId());
            statement.setObject(5, product.getProductPhoto());
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                return product.getId();
            }
        } catch (SQLException | IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void delete(UUID id) {
        String query = "DELETE FROM products WHERE id = ?";
        try (Connection connection = dataSourceConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setObject(1, id);
            statement.executeUpdate();
        } catch (SQLException | IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public List<Product> getProductsByBasketId(UUID id) {
        List<Product> products = new ArrayList<>();
        try {
            String query = "SELECT product_id FROM basket_products WHERE basket_id = ?";
            PreparedStatement productId = dataSourceConnection.getConnection().prepareStatement(query);
            productId.setObject(1, id);
            ResultSet rs = productId.executeQuery();
            while (rs.next()) {
                Product product = find((UUID) rs.getObject("product_id"));
                products.add(product);
            }

        } catch (SQLException | IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return products;
    }

    @Override
    public Product find(UUID id) {
        String query = "SELECT * FROM products WHERE id = ?";
        try (Connection connection = dataSourceConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setObject(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Product product = new Product();
                    product.setId((UUID) resultSet.getObject("id"));
                    product.setName(resultSet.getString("name"));
                    product.setPrice(resultSet.getDouble("price"));
                    product.setQuantity(resultSet.getInt("quantity"));
                    product.setCategoryId((UUID) resultSet.getObject("category_id"));
                    product.setProductPhoto(resultSet.getString("photo_link"));
                    return product;
                }
            }
        } catch (SQLException | IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Product> findAll() {
        String query = "SELECT * FROM products";
        List<Product> products = new ArrayList<>();
        try (Connection connection = dataSourceConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Product product = new Product();
                product.setId((UUID) resultSet.getObject("id"));
                product.setName(resultSet.getString("name"));
                product.setPrice(resultSet.getDouble("price"));
                product.setQuantity(resultSet.getInt("quantity"));
                product.setCategoryId((UUID) resultSet.getObject("category_id"));
                product.setProductPhoto(resultSet.getString("photo_link"));
                products.add(product);
            }
        } catch (SQLException | IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return products;
    }
}
