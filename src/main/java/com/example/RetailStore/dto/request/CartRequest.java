package com.example.RetailStore.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartRequest {

    @NotEmpty
    private String userId;

    @NotEmpty
    private List<CartItemRequest> items;
}
