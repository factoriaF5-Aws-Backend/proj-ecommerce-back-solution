package com.factoriaf5.ecommerceManager.reviews;

import com.factoriaf5.ecommerceManager.users.User;
import com.factoriaf5.ecommerceManager.users.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/reviews")
public class ReviewController {
    @Autowired
    ReviewService reviewService;

    @Autowired
    UserRepo userRepo;

    @GetMapping("/{productId}")
    public List<Review> listAllReviewsByProductId(@PathVariable Long productId) {
        return reviewService.findAllReviewsPerProduct(productId);
    }

    @GetMapping("/{productId}/{userName}")
    public List<Review> findReviewByProductIdAndUserUserName(@PathVariable Long productId, @PathVariable String userName) {
        return reviewService.findReviewByProductIdAndUserUserName(productId, userName);
    }

    @PostMapping
    public ResponseEntity<Review> createReview(@RequestBody ReviewRequest reviewRequest) {
        User authUser = userRepo.findByUserName("admin");
        ReviewRequest createdReviewRequest = new ReviewRequest(
                reviewRequest.body(),
                reviewRequest.rating(),
                authUser.getUserName(),
                reviewRequest.product_Id());
        Review createdReview = reviewService.saveReview(createdReviewRequest);
        return ResponseEntity.status(201).body(createdReview);
    }

}
