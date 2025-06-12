package com.example.RetailStore.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductResponse {
    private String productId;
    private String name;
    private String description;
    private Double price;
    private String imageUrl;
    private Integer stock;
}
