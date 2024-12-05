package org.common.repository;

import java.util.List;
import java.util.UUID;

public interface BaseRepository<T> {
    void create(T entity);
    UUID update(T entity);
    void delete(UUID id);
    T find(UUID id);
    List<T> findAll();
}
