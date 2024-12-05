package org.common.service;

import lombok.RequiredArgsConstructor;
import org.common.model.Category;
import org.common.repository.CategoryRepository;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class CategoryService implements BaseService<Category> {
    private final CategoryRepository categoryRepository;

    public void add(Category category) throws IOException {
        categoryRepository.create(category);
    }

    @Override
    public List<Category> list() throws IOException {
        return categoryRepository.findAll()
                .stream().filter(category -> category.getParentId() == null)
                .toList();
    }

    @Override
    public List<Category> getById(UUID id) throws IOException {
        List<Category> list = categoryRepository.findAll();
        return list.stream()
                .filter(category -> category.getParentId() != null && category.getParentId().equals(id))
                .toList();
    }

    public boolean hasChildCategory(UUID id) throws IOException {
        List<Category> list = categoryRepository.findAll().stream()
                .filter(category -> category.getParentId() != null).toList();

        return list.stream().anyMatch(category -> category.getParentId().equals(id));
    }
}
