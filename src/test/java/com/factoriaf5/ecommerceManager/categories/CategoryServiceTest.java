package com.factoriaf5.ecommerceManager.categories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    CategoryRepository categoryRepository;

    @InjectMocks
    CategoryService categoryService;

    @BeforeEach
    void before() {

    }

    @Test
    void can_create_valid_category() {
        CategoryRequest categoryRequest = new CategoryRequest("instruments");
        Category category = new Category(categoryRequest.name());

        Mockito.when(categoryRepository.existsByName(category.getName())).thenReturn(false);
        Mockito.when(categoryRepository.save(category)).thenReturn(category);

        assertEquals(categoryRequest.name(), categoryService.create(categoryRequest).getName());

        verify(categoryRepository, times(1)).save(category);
    }

    @Test
    void can_not_create_category_with_same_name() {
        CategoryRequest categoryRequest = new CategoryRequest("instruments");
        Category category = new Category(categoryRequest.name());

        Mockito.when(categoryRepository.existsByName(category.getName())).thenReturn(true);

        Exception ex = assertThrows(CategoryNameExistsException.class, () -> categoryService.create(categoryRequest));

        assertEquals("Category name already exists", ex.getMessage());

        verify(categoryRepository, never()).save(any(Category.class));
    }
}