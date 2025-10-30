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
    @Schema(description = "user name of account", example = "tung1st2003")
    @NotNull
    String username;

    @Schema(description = "pass word of account", example = "tung1032003")
    @NotNull
    String password;
}
