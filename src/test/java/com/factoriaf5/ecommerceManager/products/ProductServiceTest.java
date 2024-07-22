package com.factoriaf5.ecommerceManager.products;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock
    ProductRepo productRepo;

    @Test
    void testCreateProduct() {
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

    @Test
    void testThatListOfProductCanBeRetrieved() {
        ProductService productService = new ProductService(productRepo);
        Product savedProduct = new Product(1L, "test", "test", 1.0);

        List<Product> listOfProducts = List.of(savedProduct);

        Mockito.when(productRepo.findAll()).thenReturn(listOfProducts);
        List<Product> productResponse = productService.getAllProducts();

        assertEquals(listOfProducts, productResponse);

    }

    @Test
    void testAProductCanBeRetrievedById(){
        ProductService productService = new ProductService(productRepo);
        Product savedProduct = new Product(1L, "test", "test", 1.0);

        Mockito.when(productRepo.findById(1L)).thenReturn(Optional.of(savedProduct));
        Optional<Product> productResponse = productService.findProduct(1L);

        assertTrue(productResponse.isPresent());

        assertEquals(savedProduct, productResponse.get());
    }


}