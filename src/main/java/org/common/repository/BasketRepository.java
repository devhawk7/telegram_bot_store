package org.common.repository;

import lombok.extern.slf4j.Slf4j;
import org.common.config.DataSourceConnection;
import org.common.model.Basket;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
public class BasketRepository implements BaseRepository<Basket> {
    private final DataSourceConnection dataSourceConnection;

    public BasketRepository(DataSourceConnection dataSourceConnection){
        this.dataSourceConnection = dataSourceConnection;
    }

    @Override
    public void create(Basket entity) {
        String basketQuery = "INSERT INTO basket (id, user_id, total_price) " +
                "VALUES (?, ?, ?)";
        try {
            Connection connection = dataSourceConnection.getConnection();
            PreparedStatement basketStatement = connection.prepareStatement(basketQuery);
            basketStatement.setObject(1, entity.getId());
            basketStatement.setLong(2, entity.getUserId());
            basketStatement.setDouble(3, entity.getTotalPrice());
            basketStatement.executeUpdate();
        } catch (SQLException | IOException| ClassNotFoundException e) {
            log.error(e.getMessage());
        }

    }


    @Override
    public UUID update(Basket entity) {
        String query = "update basket set total_price = ? where id = ?";
        try {
            Connection connection = dataSourceConnection.getConnection();
            PreparedStatement updateQuery = connection.prepareStatement(query);
            updateQuery.setDouble(1, entity.getTotalPrice());
        } catch (SQLException | IOException| ClassNotFoundException e) {
            log.error(e.getMessage());
        }

        return null;
    }

    public void addProduct(UUID basketId, UUID productId) {
        String query = "insert into basket_products  (basket_id, product_id) values (?, ?)";
        try {
            Connection connection = dataSourceConnection.getConnection();
            PreparedStatement basketStatement = connection.prepareStatement(query);
            basketStatement.setObject(1, basketId);
            basketStatement.setObject(2, productId);
            basketStatement.executeUpdate();
        } catch (SQLException | IOException | ClassNotFoundException e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public void delete(UUID id) {
        try {
            Connection connection = dataSourceConnection.getConnection();
            String BasketQuery = "delete from basket where id = ?";
            String ProductQuery = "delete from basket_products where basket_id = ?";
            PreparedStatement basket = connection.prepareStatement(BasketQuery);
            PreparedStatement products = connection.prepareStatement(ProductQuery);
            basket.setObject(1, id);
            products.setObject(1, id);
            products.executeUpdate();
            basket.executeUpdate();
        } catch (SQLException | IOException | ClassNotFoundException e) {
            log.error(e.getMessage());
        }
    }

    public void deleteProduct(UUID id) {
        try {
            Connection connection = dataSourceConnection.getConnection();
            String ProductQuery = "delete from basket_products where product_id = ?";
            PreparedStatement products = connection.prepareStatement(ProductQuery);
            products.setObject(1, id);
            products.executeUpdate();
        } catch (SQLException | IOException | ClassNotFoundException e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public Basket find(UUID id) {
        try {
            Connection connection = dataSourceConnection.getConnection();
            String search = "select * from basket where id = ?";
            PreparedStatement basketQuery = connection.prepareStatement(search);
            basketQuery.setObject(1, id);
            ResultSet rs = basketQuery.executeQuery();
            if (rs.next()) {
                Basket basket = new Basket();
                basket.setId((UUID) rs.getObject("id"));
                basket.setUserId(rs.getLong("user_id"));
                basket.setTotalPrice(rs.getDouble("total_price"));
                return basket;
            }
        } catch (SQLException | IOException | ClassNotFoundException e) {
            log.error(e.getMessage());
        }
        return null;
    }

    @Override
    public List<Basket> findAll() {
        List<Basket> basketList = new ArrayList<>();
        try {
            Connection connection = dataSourceConnection.getConnection();
            String search = "select * from basket";

            PreparedStatement baskets = connection.prepareStatement(search);
            ResultSet rs = baskets.executeQuery();
            while (rs.next()) {
                Basket basket = new Basket();
                basket.setId((UUID) rs.getObject("id"));
                basket.setUserId(rs.getLong("user_id"));
                basket.setTotalPrice(rs.getDouble("total_price"));
                basketList.add(basket);
            }
        } catch (SQLException | IOException| ClassNotFoundException e) {
            log.error(e.getMessage());
        }
        return basketList;
    }
}
