package com.factoriaf5.ecommerceManager.reviews;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepo extends JpaRepository<Review, Long> {

    List<Review> findAllByProductId(Long product_id);


    List<Review> findByProductIdAndUserUserName(@Param("productId") Long productId, @Param("userName") String userName);
}
