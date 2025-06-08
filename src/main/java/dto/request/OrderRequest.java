package dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {
    @NotBlank
    private String shippingAddress;

    @NotBlank
    private String userId;

    @NotEmpty
    private List<CartItemRequest> items;
}
