package com.factoriaf5.ecommerceManager.products;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ProductRepo extends JpaRepository<Product, Long> {
    List<Product> findByFeaturedTrue();

    List<Product> findByCategory(String category);
}
