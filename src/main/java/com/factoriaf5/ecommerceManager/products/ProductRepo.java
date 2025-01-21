package com.factoriaf5.ecommerceManager.products;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface ProductRepo extends JpaRepository<Product, Long> {
    @Query (name = "Product.findByNamed")
    Product findProductsByName(String name);

    @Query(value = "SELECT * FROM product WHERE featured = true", nativeQuery = true)
    List<Product> findFeaturedProducts();



    @Query("SELECT p FROM Product p WHERE p.price BETWEEN :minPrice AND :maxPrice")
    List<Product> findByPriceRange(@Param("minPrice") double minPrice, @Param("maxPrice") double maxPrice);
}
