package com.example.RetailStore.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartItemRequest {
    @Schema(description = "id product client buy", example = "1")
    @NotNull
    @NotBlank
    String productId;

    @Schema(description = "quantity product", example = "1")
    @NotNull
    @Min(1)
    Integer quantity;
}
