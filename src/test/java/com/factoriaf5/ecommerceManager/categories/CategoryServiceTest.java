package com.factoriaf5.ecommerceManager.categories;

import com.factoriaf5.ecommerceManager.products.Product;
import com.factoriaf5.ecommerceManager.products.ProductNotFoundException;
import com.factoriaf5.ecommerceManager.products.ProductRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

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

    @Test
    void testACategoryCanBeRetrievedById() {
        Category category = new Category("name");
        Mockito.when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        Category categoryResponse = categoryService.findCategory(1L);

        assertEquals(category, categoryResponse);
    }

    @Test
    void testAnExceptionThrownWhenCategoryNotFound() {
        Mockito.when(categoryRepository.findById(2L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> categoryService.findCategory(2L));

        String expectedMessage = "The category with id: 2 is not found";

        assertEquals(expectedMessage, exception.getMessage());

        verify(categoryRepository, Mockito.times(1)).findById(any(Long.class));
    }

    @Test
    void testAProductCanBeDeletedUsingId() {
        Category category = new Category("name");
        Mockito.when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        categoryService.deleteCategory(1L);

        verify(categoryRepository, Mockito.times(1)).deleteById(1L);
    }

    @Test
    void testACategoryCanBeUpdated(){
        Category category = new Category("name");
        Mockito.when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        Mockito.when(categoryRepository.save(any(Category.class))).thenReturn(category);

        CategoryRequest categoryRequest = new CategoryRequest("updated");

        Optional<Category> optionalCategory = (Optional<Category>) categoryService.updateCategory(categoryRequest, 1L);

        assertTrue(optionalCategory.isPresent());
        Category updatedCategory = optionalCategory.get();
        assertEquals("updated", updatedCategory.getName());
    }
}