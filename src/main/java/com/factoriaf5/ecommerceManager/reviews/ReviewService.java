package com.factoriaf5.ecommerceManager.reviews;

import com.factoriaf5.ecommerceManager.products.Product;
import com.factoriaf5.ecommerceManager.products.ProductService;
import com.factoriaf5.ecommerceManager.users.User;
import com.factoriaf5.ecommerceManager.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {
    @Autowired
    ReviewRepo reviewRepo;
    @Autowired
    UserService userService;
    @Autowired
    ProductService productService;

    public List<Review> findAllReviewsPerProduct(Long productId) {
        return reviewRepo.findAllByProductId(productId);
    }

    public Review saveReview(ReviewRequest reviewRequest) {
        User user = userService.findUser(reviewRequest.app_user_userName());
        Product product = productService.findProduct(reviewRequest.product_Id());

        List<Review> checkForReviews = reviewRepo.findByProductIdAndUserUserName(product.getId(), user.getUserName());

        if (!checkForReviews.isEmpty()) {
            throw new RuntimeException("The user can only make one review, you already added a review");
        }

        Review review = new Review(
                reviewRequest.body(),
                reviewRequest.rating(),
                user,
                product
        );
        return reviewRepo.save(review);
    }

    public List<Review> findReviewByProductIdAndUserUserName(Long productId, String userName) {
        return reviewRepo.findByProductIdAndUserUserName(productId, userName);
    }
}
