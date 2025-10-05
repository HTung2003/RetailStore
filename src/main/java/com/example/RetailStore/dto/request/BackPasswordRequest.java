package com.example.RetailStore.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BackPasswordRequest {
    @NotBlank(message = "USER_NOT_BLANK")
    String username;

    @Email(message = "EMAIL_INVALID")
    @NotBlank(message = "EMAIL_NOT_BLANK")
    String email;
}
