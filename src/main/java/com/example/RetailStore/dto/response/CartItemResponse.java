package com.example.RetailStore.dto.response;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartItemResponse {
    private String itemId;
    private ProductItemResponse product;
    private Integer quantity;
}
