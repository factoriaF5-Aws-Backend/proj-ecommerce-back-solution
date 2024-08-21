package com.factoriaf5.ecommerceManager.products;


import org.springframework.web.multipart.MultipartFile;

public record ProductRequest(
        String name,
        String description,
        Double price,
        Boolean featured,
        MultipartFile image,
        //TODO modify to work with category_id
        String category
) {

}
