package com.factoriaf5.ecommerceManager.products;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ProductDataInitializer implements CommandLineRunner {

    private final ProductRepo productRepository;

    public ProductDataInitializer(ProductRepo productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Check if there are already products in the database
        if (productRepository.count() == 0) {
            // Create and save 10 products
            Product product1 = new Product(1L, "Smartphone", "Latest model with high-resolution camera", 799.99, true, "https://example.com/images/smartphone.jpg", "Electronics");
            Product product2 = new Product(2L, "Laptop", "Powerful laptop for professionals", 1299.99, false, "https://example.com/images/laptop.jpg", "Electronics");
            Product product3 = new Product(3L, "Coffee Maker", "High-quality coffee maker for home use", 89.99, true, "https://example.com/images/coffeemaker.jpg", "Home Appliances");
            Product product4 = new Product(4L, "Headphones", "Noise-canceling over-ear headphones", 199.99, true, "https://example.com/images/headphones.jpg", "Audio");
            Product product5 = new Product(5L, "Smartwatch", "Fitness tracking smartwatch with heart rate monitor", 149.99, false, "https://example.com/images/smartwatch.jpg", "Wearables");
            Product product6 = new Product(6L, "TV", "55-inch 4K Ultra HD Smart TV", 799.99, true, "https://example.com/images/tv.jpg", "Electronics");
            Product product7 = new Product(7L, "Gaming Console", "Latest generation gaming console with controllers", 499.99, true, "https://example.com/images/console.jpg", "Gaming");
            Product product8 = new Product(8L, "Microwave Oven", "Compact microwave oven with multiple settings", 99.99, false, "https://example.com/images/microwave.jpg", "Home Appliances");
            Product product9 = new Product(9L, "Bluetooth Speaker", "Portable Bluetooth speaker with deep bass", 59.99, false, "https://example.com/images/speaker.jpg", "Audio");
            Product product10 = new Product(10L, "Tablet", "10-inch tablet with high resolution display", 399.99, true, "https://example.com/images/tablet.jpg", "Electronics");

            productRepository.save(product1);
            productRepository.save(product2);
            productRepository.save(product3);
            productRepository.save(product4);
            productRepository.save(product5);
            productRepository.save(product6);
            productRepository.save(product7);
            productRepository.save(product8);
            productRepository.save(product9);
            productRepository.save(product10);

            System.out.println("10 sample products have been created in the database.");
        } else {
            System.out.println("Products already exist in the database.");
        }
    }
}

