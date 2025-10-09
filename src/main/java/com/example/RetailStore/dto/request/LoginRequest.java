package com.example.RetailStore.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoginRequest {
    @Schema(description = "user name of account", example = "tungst01")
    @NotNull
    String username;

    @Schema(description = "pass word of account", example = "12345678")
    @NotNull
    String password;
}
