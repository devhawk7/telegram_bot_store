package org.common.repository;

import lombok.extern.slf4j.Slf4j;
import org.common.config.DataSourceConnection;
import org.common.model.Category;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
public class CategoryRepository implements BaseRepository<Category> {
    private final DataSourceConnection dataSourceConnection;
    public CategoryRepository(DataSourceConnection dataSourceConnection) {
        this.dataSourceConnection = dataSourceConnection;
    }

    @Override
    public void create(Category entity) {
        String query = "insert into categories (id, name,parent_id) values (?, ?,?)";
        try {
            Connection connection = dataSourceConnection.getConnection();
            PreparedStatement createQuery = connection.prepareStatement(query);
            createQuery.setObject(1, entity.getId());
            createQuery.setObject(2, entity.getName());
            createQuery.setObject(3, entity.getParentId());
            createQuery.executeUpdate();
        } catch (SQLException | IOException | ClassNotFoundException e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public UUID update(Category entity) {
        String query = "update categories set name = ?, parent_id = ? where id = ?;";
        try {
            Connection connection = dataSourceConnection.getConnection();
            PreparedStatement createQuery = connection.prepareStatement(query);
            createQuery.setObject(1, entity.getId());
            createQuery.setObject(2, entity.getName());
            createQuery.setObject(3, entity.getParentId());
            createQuery.executeUpdate();
            return entity.getId();
        } catch (SQLException | IOException | ClassNotFoundException e) {
            log.error(e.getMessage());
        }
        return null;
    }

    @Override
    public void delete(UUID id) {
        String query = "delete from categories where id = ?;";
        try {
            Connection connection = dataSourceConnection.getConnection();
            PreparedStatement createQuery = connection.prepareStatement(query);
            createQuery.setObject(1, id);
            createQuery.executeUpdate();
        } catch (SQLException | IOException | ClassNotFoundException e) {
            log.error(e.getMessage());
        }

    }

    @Override
    public Category find(UUID id) {
        String query = "select * from categories where id = ?;";
        try {
            Connection connection = dataSourceConnection.getConnection();
            PreparedStatement createQuery = connection.prepareStatement(query);
            createQuery.setObject(1, id);
            ResultSet resultSet = createQuery.executeQuery();
            if (resultSet.next()) {
                Category category = new Category();
                category.setId((UUID) resultSet.getObject("id"));
                category.setName(resultSet.getString("name"));
                category.setParentId((UUID) resultSet.getObject("parent_id"));
                return category;
            }
        } catch (SQLException | IOException | ClassNotFoundException e) {
            log.error(e.getMessage());
        }
        return null;
    }

    @Override
    public List<Category> findAll() {
        String query = "select * from categories;";
        try {
            Connection connection = dataSourceConnection.getConnection();
            PreparedStatement createQuery = connection.prepareStatement(query);
            ResultSet resultSet = createQuery.executeQuery();
            List<Category> categories = new ArrayList<>();
            while (resultSet.next()) {
                Category category = new Category();
                category.setId((UUID) resultSet.getObject("id"));
                category.setName(resultSet.getString("name"));
                category.setParentId((UUID) resultSet.getObject("parent_id"));
                categories.add(category);
            }
            return categories;
        } catch (SQLException | IOException | ClassNotFoundException e) {
            log.error(e.getMessage());
        }
        return List.of();
    }
}
