package com.factoriaf5.ecommerceManager.products;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepo productRepo;

    public ProductService(ProductRepo productRepo) {
        this.productRepo = productRepo;
    }

    public Product saveProduct(ProductRequest productRequest) {
        Product product = new Product(productRequest.name(), productRequest.description(), productRequest.price());

        return productRepo.save(product);
    }

    public List<Product> getAllProducts() {
        return productRepo.findAll();
    }

    public Product findProduct(long id) {
        Optional<Product> returnedProduct = productRepo.findById(id);
        if (returnedProduct.isEmpty()) {
            throw new ProductNotFoundException("The product with id: " + id + " is not found");
        }
        return returnedProduct.get();
    }

    public void deleteProduct(Long id) {
        productRepo.deleteById(id);
    }

    public Optional<Product> updateProduct(ProductRequest productRequest, Long id) {
        Optional<Product> returnedProduct = productRepo.findById(id);
        if (returnedProduct.isEmpty()) {
            throw new ProductNotFoundException("The product with id: " + id + " is not found");
        }

        Product updatedProduct = returnedProduct.get();

        updatedProduct.setName(productRequest.name());
        updatedProduct.setDescription(productRequest.description());
        updatedProduct.setPrice(productRequest.price());

        return Optional.of(productRepo.save(updatedProduct));
    }
}
