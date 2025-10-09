package com.example.RetailStore.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductRequest {
    @Schema(description = "name product", example = "fruit")
    @NotBlank
    String name;

    @Schema(description = "description about product")
    String description;

    @Schema(description = "price")
    @NotNull
    @Positive
    Double price;

    String imageUrl;

    @NotNull
    @Min(0)
    Integer stock;
}
