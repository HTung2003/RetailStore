package com.example.RetailStore.dto.request;

import com.example.RetailStore.enums.Role;
import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserRequest {

    @Column(unique = true)
    @NotBlank(message = "USERNAME_NOT_BLANK")
    @Size(min=4,max = 32, message = "USERNAME_INVALID")
    String username;

    @NotBlank(message = "PASSWORD_NOT_BLANK")
    @Size(min=8,max =32,message = "PASSWORD_INVALID")
    String password;

    @NotBlank(message = "EMAIL_NOT_BLANK")
    @Email(message = "EMAIL_INVALID")
    String email;

    @NotBlank(message = "PHONE_NOT_BLANK")
    @Pattern(regexp = "\\d+",message = "PHONE_INVALID")
    String phone;

    @NotBlank(message = "ADDRESS_NOT_BLANK")
    String address;

    @NotNull(message = "ROLE_NOT_BLANK")
    Role role;
}
