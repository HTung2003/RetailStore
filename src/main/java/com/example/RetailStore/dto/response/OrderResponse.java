package com.example.RetailStore.dto.response;

import com.example.RetailStore.enums.OrderStatus;
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
    private UserInfo user;
    private LocalDateTime orderDate;
    private String shippingAddress;
    private OrderStatus status;
    private String code;
    private List<OrderItemResponse> orderItems;
}
