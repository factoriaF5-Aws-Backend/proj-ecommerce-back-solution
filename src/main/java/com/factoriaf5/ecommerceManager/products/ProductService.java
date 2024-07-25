package com.factoriaf5.ecommerceManager.products;

import com.factoriaf5.ecommerceManager.files.FileStorageService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepo productRepo;
    private final FileStorageService fileStorageService;

    private static final String IMAGE_DIRECTORY = "uploads/images/";


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
                imageUrl
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
        productRepo.deleteById(id);
    }

    public Optional<Product> updateProduct(ProductRequest productRequest, Long id) throws IOException {
        Optional<Product> returnedProduct = productRepo.findById(id);
        if (returnedProduct.isEmpty()) {
            throw new ProductNotFoundException("The product with id: " + id + " is not found");
        }

        Product updatedProduct = returnedProduct.get();

        if (productRequest.image().isEmpty()) {
            Path path = null;
        }
        byte[] bytes = productRequest.image().getBytes();
        Path path = Path.of(IMAGE_DIRECTORY + productRequest.image().getOriginalFilename());

        // Create directories if they do not exist
        Files.createDirectories(path.getParent());

        Files.write(path, bytes);
        String imagePath = path.toString();

        updatedProduct.setName(productRequest.name());
        updatedProduct.setDescription(productRequest.description());
        updatedProduct.setPrice(productRequest.price());
        updatedProduct.setFeatured(productRequest.featured());
        updatedProduct.setImage(imagePath);

        return Optional.of(productRepo.save(updatedProduct));
    }


}
