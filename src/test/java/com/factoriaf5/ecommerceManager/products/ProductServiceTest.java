package com.factoriaf5.ecommerceManager.products;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock
    ProductRepo productRepo;
    @Test
    void testCreateProduct() {
        ProductService productService = new ProductService(productRepo);
        Product product = new Product("test", "test", 1.0);
        Product savedProduct = new Product(1L,"test", "test", 1.0);

        Mockito.when(productRepo.save(product)).thenReturn(savedProduct);
        Product productResponse = productService.saveProduct(product);

        assertEquals(1L,productResponse.getId());
        assertEquals(product.getName(), productResponse.getName());
        assertEquals(product.getPrice(),productResponse.getPrice());
        assertEquals(product.getDescription(),productResponse.getDescription());
    }
}