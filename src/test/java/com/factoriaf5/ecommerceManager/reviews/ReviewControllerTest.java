package com.factoriaf5.ecommerceManager.reviews;


import com.factoriaf5.ecommerceManager.products.Product;
import com.factoriaf5.ecommerceManager.products.ProductRepo;
import com.factoriaf5.ecommerceManager.users.User;
import com.factoriaf5.ecommerceManager.users.UserRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ReviewControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ReviewRepo reviewRepo;

    @Autowired
    UserRepo userRepo;

    @Autowired
    ProductRepo productRepo;

    ObjectMapper objectMapper = new ObjectMapper();
    @Test
    void createReview() throws Exception{
        User user = new User(1L, "user", "password", "user@email.com");
        Product product = new Product(1L, "product", "description", 1.0, true, "url");
        ReviewRequest reviewRequest = new ReviewRequest("a new product", 4.0, user.getUserName(), product.getId());

        userRepo.save(user);
        productRepo.save(product);

        String reviewRequestJson = objectMapper.writeValueAsString(reviewRequest);
        mockMvc.perform(post("/api/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(reviewRequestJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").value(1L))
                .andExpect(jsonPath("body").value("a new product"))
                .andExpect(jsonPath("rating").value(4.0))
                .andExpect(jsonPath("$.user.userName").value(user.getUserName()))
                .andExpect(jsonPath("$.user.password").value(user.getPassword()))
                .andExpect(jsonPath("$.product.id").value(product.getId()))
                .andExpect(jsonPath("$.product.name").value(product.getName()))
        ;
    }

    @Test
    void findAllReviewsPerProduct() throws Exception {
        User user = new User(1L, "user", "password", "user@email.com");
        Product product = new Product(1L, "product", "description", 1.0, true, "url");

        userRepo.save(user);
        productRepo.save(product);

        Review savedReview = new Review(1L, "a new product", 4.0, user, product);

        reviewRepo.save(savedReview);

        mockMvc.perform(get("/api/reviews/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].body").value("a new product"))
                .andExpect(jsonPath("$[0].rating").value(4.0))
                .andExpect(jsonPath("$[0].user.userName").value(user.getUserName()))
                .andExpect(jsonPath("$[0].user.email").value(user.getEmail()))
                .andExpect(jsonPath("$[0].product.id").value(product.getId()))
                .andExpect(jsonPath("$[0].product.name").value(product.getName()))
        ;
    }

    @Test
    void findReviewByProductIdAndUserUserName() throws Exception {
        User user = new User(1L, "user", "password", "user@email.com");
        Product product = new Product(1L, "product", "description", 1.0, true, "url");

        userRepo.save(user);
        productRepo.save(product);

        Review savedReview = new Review(1L, "a new product", 4.0, user, product);

        reviewRepo.save(savedReview);

        mockMvc.perform(get("/api/reviews/1/user")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].body").value("a new product"))
                .andExpect(jsonPath("$[0].rating").value(4.0))
                .andExpect(jsonPath("$[0].user.userName").value(user.getUserName()))
                .andExpect(jsonPath("$[0].user.email").value(user.getEmail()))
                .andExpect(jsonPath("$[0].product.id").value(product.getId()))
                .andExpect(jsonPath("$[0].product.name").value(product.getName()))
        ;
    }
}
