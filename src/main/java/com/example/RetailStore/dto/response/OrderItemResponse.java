package com.example.RetailStore.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemResponse {
    private String orderItemId;
    private String productId;
    private String productName;
    private Integer quantity;
    private Double price;
}
