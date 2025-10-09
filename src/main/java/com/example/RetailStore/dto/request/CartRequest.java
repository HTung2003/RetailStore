package com.example.RetailStore.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartRequest {

    @Schema(description = "userId own cart")
    @NotEmpty
    private String userId;

    @Schema(description = "List item")
    @NotEmpty
    private List<CartItemRequest> items;
}
