package com.factoriaf5.ecommerceManager.products;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
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
    //Create a Mock for the repository
    @Mock
    ProductRepo productRepo;
    //Inject the service
    @InjectMocks
    ProductService productService;

    @Test
    void testCreateProduct() {
        ProductRequest productRequest = new ProductRequest("test", "test", 1.0, true);
        Product savedProduct = new Product(1L, "test", "test", 1.0, true);

        Mockito.when(productRepo.save(any(Product.class))).thenReturn(savedProduct);
        Product productResponse = productService.saveProduct(productRequest);

        assertEquals(1L, productResponse.getId());
        assertEquals(productRequest.name(), productResponse.getName());
        assertEquals(productRequest.price(), productResponse.getPrice());
        assertEquals(productRequest.description(), productResponse.getDescription());
        assertEquals(productRequest.featured(), productResponse.getFeatured());

        verify(productRepo).save(any(Product.class));
    }

    @Test
    void testThatPriceCanNotBeNegative() {
        ProductRequest productRequest = new ProductRequest("test", "test", -1.0, true);

        Exception exception = assertThrows(NegativePriceException.class, () -> productService.saveProduct(productRequest));

        String expectedMessage = "The price must be positive";

        assertEquals(expectedMessage, exception.getMessage());

        verify(productRepo, Mockito.never()).save(any(Product.class));
    }

    @Test
    void testThatListOfProductCanBeRetrieved() {
        Product savedProduct = new Product(1L, "test", "test", 1.0, true);

        List<Product> listOfProducts = List.of(savedProduct);

        Mockito.when(productRepo.findAll()).thenReturn(listOfProducts);
        List<Product> productResponse = productService.getAllProducts();

        assertEquals(listOfProducts, productResponse);

    }

    @Test
    void testAProductCanBeRetrievedById() {
        Product savedProduct = new Product(1L, "test", "test", 1.0, true);

        Mockito.when(productRepo.findById(1L)).thenReturn(Optional.of(savedProduct));
        Product productResponse = productService.findProduct(1L);

        assertEquals(savedProduct, productResponse);
    }

    @Test
    void testAProductCanNotBeFoundException() {
        Mockito.when(productRepo.findById(2L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ProductNotFoundException.class, () -> productService.findProduct(2L));

        String expectedMessage = "The product with id: 2 is not found";

        assertEquals(expectedMessage, exception.getMessage());

        verify(productRepo, Mockito.times(1)).findById(any(Long.class));
    }

    @Test
    void testAProductCanBeDeletedUsingId() {
        productService.deleteProduct(1L);

        verify(productRepo, Mockito.times(1)).deleteById(1L);
    }

    @Test
    void testAProductCanBeUpdated(){
        Product savedProduct = new Product(1L, "test", "test", 1.0, true);

        ProductRequest productRequest = new ProductRequest("updated", "updated", 2.0, false);

        Mockito.when(productRepo.findById(1L)).thenReturn(Optional.of(savedProduct));
        Mockito.when(productRepo.save(any(Product.class))).thenReturn(savedProduct);

        Optional<Product> optionalProduct = productService.updateProduct(productRequest, 1L);

        assertTrue(optionalProduct.isPresent());
        Product updatedProduct = optionalProduct.get();
        assertEquals("updated", updatedProduct.getName());
        assertEquals("updated", updatedProduct.getDescription());
        assertEquals(2.0, updatedProduct.getPrice());
        assertEquals(false, updatedProduct.getFeatured());
    }



}