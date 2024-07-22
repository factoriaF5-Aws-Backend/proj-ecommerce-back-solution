package com.factoriaf5.ecommerceManager.products;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock
    ProductRepo productRepo;

    @Test
    void testCreateProduct(){
        ProductService productService = new ProductService(productRepo);
        ProductRequest productRequest = new ProductRequest("test", "test", 1.0);
        Product savedProduct = new Product(1L, "test", "test", 1.0);

        Mockito.when(productRepo.save(any(Product.class))).thenReturn(savedProduct);
        Product productResponse = productService.saveProduct(productRequest);

        assertEquals(1L, productResponse.getId());
        assertEquals(productRequest.name(), productResponse.getName());
        assertEquals(productRequest.price(), productResponse.getPrice());
        assertEquals(productRequest.description(), productResponse.getDescription());

        verify(productRepo).save(any(Product.class));
    }

    @Test
    void testThatPriceCanNotBeNegative() {
        ProductService productService = new ProductService(productRepo);
        ProductRequest productRequest = new ProductRequest("test", "test", -1.0);

        Exception exception = assertThrows(NegativePriceException.class, () -> productService.saveProduct(productRequest));

        String expectedMessage = "The price must be positive";

        assertEquals(expectedMessage, exception.getMessage());

        verify(productRepo, Mockito.never()).save(any(Product.class));
    }




}