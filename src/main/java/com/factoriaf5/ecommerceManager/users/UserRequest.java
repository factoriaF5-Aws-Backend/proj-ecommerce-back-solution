package com.factoriaf5.ecommerceManager.users;

public record UserRequest(
        String userName,
        String email,
        String password
) {
}

