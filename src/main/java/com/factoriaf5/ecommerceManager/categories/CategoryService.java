package com.factoriaf5.ecommerceManager.categories;

import com.factoriaf5.ecommerceManager.products.Product;
import com.factoriaf5.ecommerceManager.products.ProductNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public Category findCategory(Long id){
        Optional<Category> returnedCategory = categoryRepository.findById(id);
        if (returnedCategory.isEmpty()) {
            throw new RuntimeException("The category with id: " + id + " is not found");
        }
        return returnedCategory.get();
    };

    public void deleteCategory(Long id) {
        Optional<Category> categoryToDelete = Optional.ofNullable(categoryRepository.findById(id).orElse(null));
        if (categoryToDelete != null) {
            categoryRepository.deleteById(id);
        }
    }


    public Object updateCategory(CategoryRequest categoryRequest, Long id) {
        Optional<Category> returnedCategory = categoryRepository.findById(id);

        if (returnedCategory.isEmpty()) {
            throw new RuntimeException("The Category with id: " + id + " is not found");
        }

        Category updatedCategory = returnedCategory.get();

        updatedCategory.setName(categoryRequest.name());

        return Optional.of(categoryRepository.save(updatedCategory));
    }
}
