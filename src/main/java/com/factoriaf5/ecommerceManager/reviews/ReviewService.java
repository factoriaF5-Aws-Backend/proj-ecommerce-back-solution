package com.factoriaf5.ecommerceManager.reviews;

import com.factoriaf5.ecommerceManager.products.Product;
import com.factoriaf5.ecommerceManager.products.ProductService;
import com.factoriaf5.ecommerceManager.users.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {
    @Autowired
    ReviewRepo reviewRepo;
    @Autowired
    ProductService productService;

    @Autowired
    UserRepo userRepo;

    public List<Review> findAllReviewsPerProduct(Long productId) {
        return reviewRepo.findAllByProductId(productId);
    }

    public Review saveReview(ReviewRequest reviewRequest) {
        Product product = productService.findProduct(reviewRequest.product_Id());

        List<Review> checkForReviews = reviewRepo.findByProductIdAndUserUserName(product.getId(), reviewRequest.app_user_userName());

        if (checkForReviews.size() != 0) {
            throw new RuntimeException("The user can only make one review, you already added a review");
        }

        Review review = new Review(
                reviewRequest.body(),
                reviewRequest.rating(),
                userRepo.findByUserName(reviewRequest.app_user_userName()),
                product
        );
        return reviewRepo.save(review);
    }

    public List<Review> findReviewByProductIdAndUserUserName(Long productId, String userName) {
        return reviewRepo.findByProductIdAndUserUserName(productId, userName);
    }
}
