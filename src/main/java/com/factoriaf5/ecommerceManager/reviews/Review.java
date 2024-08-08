package com.factoriaf5.ecommerceManager.reviews;

import com.factoriaf5.ecommerceManager.products.Product;
import com.factoriaf5.ecommerceManager.users.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

@Entity
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String body;
    @Min(1)
    @Max(5)
    private Double rating;

    @ManyToOne
    @JoinColumn(name = "app_user_userName")
    private User user;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    public Review(String body, Double rating, User user, Product product) {
        this.body = body;
        this.rating = rating;
        this.user = user;
        this.product = product;
    }

    public Review() {
    }

    public Review(Long id, String body, Double rating, User user, Product product) {
        this(body, rating, user, product);
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getBody() {
        return body;
    }

    public Double getRating() {
        return rating;
    }

    public User getUser() {
        return user;
    }

    public Product getProduct() {
        return product;
    }


}
