package com.factoriaf5.ecommerceManager.products;

import com.factoriaf5.ecommerceManager.files.FileStorageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    //Create a Mock for the repository
    @Mock
    ProductRepo productRepo;
    @Mock
    FileStorageService fileStorageService;
    //Inject the service
    @InjectMocks
    ProductService productService;
    ProductRequest productRequest;
    MultipartFile mockImage;
    Product savedProduct;

    @BeforeEach
    public void beforeEachTest(){
        this.mockImage = mock(MultipartFile.class);
        this.productRequest = new ProductRequest("test", "test", 1.0, true, mockImage);
        this.savedProduct = new Product(1L, "test", "test", 1.0, true, "http://localhost:8080/uploads/images/test.jpg");
    }
    @Test
    void testCreateProduct() {
        Mockito.when(fileStorageService.fileStore(mockImage)).thenReturn("url");

        Mockito.when(productRepo.save(any(Product.class))).thenReturn(savedProduct);

        Product productResponse = productService.saveProduct(productRequest);

        assertEquals(1L, productResponse.getId());
        assertEquals(productRequest.name(), productResponse.getName());
        assertEquals(productRequest.price(), productResponse.getPrice());
        assertEquals(productRequest.description(), productResponse.getDescription());
        assertEquals(productRequest.featured(), productResponse.getFeatured());
        assertEquals("http://localhost:8080/uploads/images/test.jpg", productResponse.getImageUrl());

        verify(productRepo).save(any(Product.class));
        verify(fileStorageService).fileStore(mockImage);
    }

    @Test
    void testThatPriceCanNotBeNegative() {
        ProductRequest productRequest = new ProductRequest("test", "test", -1.0, true, mockImage);

        Exception exception = assertThrows(NegativePriceException.class, () -> productService.saveProduct(productRequest));

        String expectedMessage = "The price must be positive";

        assertEquals(expectedMessage, exception.getMessage());

        verify(productRepo, Mockito.never()).save(any(Product.class));
    }

    @Test
    void testThatListOfProductCanBeRetrieved() {
        List<Product> listOfProducts = List.of(savedProduct);

        Mockito.when(productRepo.findAll()).thenReturn(listOfProducts);
        List<Product> productResponse = productService.getAllProducts();

        assertEquals(listOfProducts, productResponse);
    }

    @Test
    void testAProductCanBeRetrievedById() {
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
        Mockito.when(productRepo.findById(savedProduct.getId())).thenReturn(Optional.of(savedProduct));

        productService.deleteProduct(savedProduct.getId());

        verify(fileStorageService).fileDelete(savedProduct.getImageUrl());
        verify(productRepo, Mockito.times(1)).deleteById(1L);
    }

    @Test
    void testAProductCanBeUpdated(){
        Mockito.when(productRepo.findById(1L)).thenReturn(Optional.of(savedProduct));
        Mockito.when(productRepo.save(any(Product.class))).thenReturn(savedProduct);

        ProductRequest productRequest = new ProductRequest("updated", "updated", 2.0, false, mockImage);

        Optional<Product> optionalProduct = productService.updateProduct(productRequest, 1L);

        assertTrue(optionalProduct.isPresent());
        Product updatedProduct = optionalProduct.get();
        assertEquals("updated", updatedProduct.getName());
        assertEquals("updated", updatedProduct.getDescription());
        assertEquals(2.0, updatedProduct.getPrice());
        assertEquals(false, updatedProduct.getFeatured());
    }

    @Test
    void testThatListOfFeaturedProductCanBeRetrieved() {
        List<Product> listOfProducts = List.of(savedProduct);

        Mockito.when(productRepo.findByFeaturedTrue()).thenReturn(listOfProducts);
        List<Product> productResponse = productService.getFeaturedProducts();

        assertEquals(listOfProducts, productResponse);
    }

}