package com.factoriaf5.ecommerceManager.reviews;

import com.factoriaf5.ecommerceManager.products.Product;
import com.factoriaf5.ecommerceManager.products.ProductService;
import com.factoriaf5.ecommerceManager.users.User;
import com.factoriaf5.ecommerceManager.users.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceTest {

    @Mock
    ReviewRepo reviewRepo;

    @Mock
    UserService userService;

    @Mock
    ProductService productService;

    @InjectMocks
    ReviewService reviewService;

    private ReviewRequest reviewRequest;
    private Review savedReview;
    private User user;
    private Product product;
    private Review review;

    @BeforeEach
    void setUp() {
        user = new User(1L, "user", "email", "password");
        product = new Product(1L, "test", "test", 1.0, true, "http://localhost:8080/uploads/images/test.jpg");
        reviewRequest = new ReviewRequest("body", 5.0, user.getUserName(), 1L);
        savedReview = new Review(1L, "body", 5.0, user, product);

        Mockito.when(userService.findUser(user.getUserName())).thenReturn(user);
        Mockito.when(productService.findProduct(product.getId())).thenReturn(product);
        Mockito.when(reviewRepo.findByProductIdAndUserUserName(product.getId(), user.getUserName())).thenReturn(List.of());
        Mockito.when(reviewRepo.save(any(Review.class))).thenReturn(savedReview);

        review = reviewService.saveReview(reviewRequest);
    }

    @Test
    void testCreateReview() {
        assertEquals(savedReview, review);
        Mockito.verify(reviewRepo).save(any(Review.class));
    }

    @Test
    void testReviewCanNotBeCreatedTwiceByTheSameUser(){
        Mockito.when(reviewRepo.findByProductIdAndUserUserName(1L,"user")).thenReturn(List.of(savedReview));

        Exception exception = assertThrows(RuntimeException.class, () -> reviewService.saveReview(reviewRequest));

        String expectedMessage = "The user can only make one review, you already added a review";

        assertEquals(expectedMessage, exception.getMessage());
    }
    @Test
    void testFindReviewsByProductId(){
        List<Review> listOfReviews = List.of(savedReview);

        Mockito.when(reviewRepo.findAllByProductId(product.getId())).thenReturn(listOfReviews);
        List<Review> reviewResponse = reviewService.findAllReviewsPerProduct(product.getId());

        assertEquals(listOfReviews, reviewResponse);
    }

    @Test
    void testFindReviewByProductIdAndUserUserName(){
        Mockito.when(reviewRepo.findByProductIdAndUserUserName(product.getId(),user.getUserName())).thenReturn(List.of(savedReview));

        List<Review> reviewResponse = reviewService.findReviewByProductIdAndUserUserName(product.getId(), user.getUserName());
        assertEquals(reviewResponse, reviewResponse);
    }
}
