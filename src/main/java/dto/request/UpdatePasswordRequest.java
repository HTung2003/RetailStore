package dto.request;

import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdatePasswordRequest {
    String username;
    @Size(min=8,max =32,message = "PASSWORD_INVALID")
    String oldPassword;
    @Size(min=8,max =32,message = "PASSWORD_INVALID")
    String newPassword;
}
