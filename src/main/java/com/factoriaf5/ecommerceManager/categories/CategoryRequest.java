package com.factoriaf5.ecommerceManager.categories;

import jakarta.validation.constraints.NotBlank;

public record CategoryRequest(
        @NotBlank
        String name) {
}
