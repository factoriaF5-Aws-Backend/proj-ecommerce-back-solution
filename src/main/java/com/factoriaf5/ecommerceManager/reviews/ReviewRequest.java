package com.factoriaf5.ecommerceManager.reviews;

public record ReviewRequest(
        String body,
        Double rating,
        String app_user_userName,
        Long product_Id
) {
}
