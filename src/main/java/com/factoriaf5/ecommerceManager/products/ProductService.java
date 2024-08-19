package com.factoriaf5.ecommerceManager.products;

import com.factoriaf5.ecommerceManager.files.FileStorageService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepo productRepo;
    private final FileStorageService fileStorageService;


    public ProductService(ProductRepo productRepo, FileStorageService fileStorageService) {
        this.productRepo = productRepo;
        this.fileStorageService = fileStorageService;
    }

    public Product saveProduct(ProductRequest productRequest) {
        String imageUrl = fileStorageService.fileStore(productRequest.image());
        Product product = new Product(
                productRequest.name(),
                productRequest.description(),
                productRequest.price(),
                productRequest.featured(),
                imageUrl,
                productRequest.category()
        );
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
        Optional<Product> productToDelete = Optional.ofNullable(productRepo.findById(id).orElse(null));
        if (productToDelete != null) {
            String imageToBeDeletedPath = productToDelete.get().getImageUrl();
            // Delete the image file
            fileStorageService.fileDelete(imageToBeDeletedPath);
            // Delete the product
            productRepo.deleteById(id);
        } else {
            throw new RuntimeException("The product with id: " + id + " is not found");
        }
    }

    public Optional<Product> updateProduct(ProductRequest productRequest, Long id) {
        String imageUrl = fileStorageService.fileStore(productRequest.image());
        Optional<Product> returnedProduct = productRepo.findById(id);

        if (returnedProduct.isEmpty()) {
            throw new ProductNotFoundException("The product with id: " + id + " is not found");
        }

        Product updatedProduct = returnedProduct.get();

        updatedProduct.setName(productRequest.name());
        updatedProduct.setDescription(productRequest.description());
        updatedProduct.setPrice(productRequest.price());
        updatedProduct.setFeatured(productRequest.featured());
        updatedProduct.setImage(imageUrl);
        updatedProduct.setCategory(productRequest.category());

        return Optional.of(productRepo.save(updatedProduct));
    }

    public List<Product> getFeaturedProducts() {
        return productRepo.findByFeaturedTrue();
    }

    public List<Product> getProductsByCategory(String category) {
        return productRepo.findByCategory(category);
    }
}
