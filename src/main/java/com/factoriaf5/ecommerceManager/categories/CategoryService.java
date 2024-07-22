package com.factoriaf5.ecommerceManager.categories;

import org.springframework.stereotype.Service;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category create(CategoryRequest request) {

        if (categoryRepository.existsByName(request.name())) {
            throw new CategoryNameExistsException("Category name already exists");
        }

        Category category = new Category(request.name());

        return this.categoryRepository.save(category);
    }
}
