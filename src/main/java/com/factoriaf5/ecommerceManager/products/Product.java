package com.factoriaf5.ecommerceManager.products;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;


@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private Double price;
    private Boolean featured = false;
    private String imageUrl;

    public Product(String name, String description, Double price, Boolean featured, String imageUrl) {
        this.name = name;
        this.description = description;
        if (price < 0) {
            throw new NegativePriceException("The price must be positive");
        }
        this.price = price;
        this.featured = featured;
        this.imageUrl = imageUrl;
    }

    public Product(Long id, String name, String description, Double price, Boolean featured, String imageUrl) {
        this(name, description, price, featured, imageUrl);
        this.id = id;
    }

    //Hibernate error
    public Product() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Double getPrice() {
        return price;
    }

    public Boolean getFeatured() {
        return featured;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setFeatured(Boolean featured) {
        this.featured = featured;
    }

    public void setImage(String imagePath) {
        this.imageUrl = imagePath;
    }
}
