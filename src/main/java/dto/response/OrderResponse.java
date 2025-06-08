package dto.response;

import enums.OrderStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {
    private String orderId;
    private String userId;
    private LocalDateTime orderDate;
    private String shippingAddress;
    private OrderStatus status;
    private List<OrderItemResponse> orderItems;
}
