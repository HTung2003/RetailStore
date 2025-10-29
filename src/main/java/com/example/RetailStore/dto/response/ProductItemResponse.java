package com.example.RetailStore.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductItemResponse {
    private String productId;
    private String name;
    private String description;
    private Double price;
    private String imageUrl;
}
