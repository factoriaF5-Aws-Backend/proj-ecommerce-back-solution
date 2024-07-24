package com.factoriaf5.ecommerceManager.products;


public record ProductRequest(
        String name,
        String description,
        Double price,
        Boolean featured
) {

}
