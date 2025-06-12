package com.example.RetailStore.controller;

import com.example.RetailStore.dto.request.OrderRequest;
import com.example.RetailStore.dto.request.UpdateOrderStatusRequest;
import com.example.RetailStore.dto.response.ApiResponse;
import com.example.RetailStore.dto.response.OrderResponse;
import com.example.RetailStore.enums.OrderStatus;
import com.example.RetailStore.service.OrderService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class OrderController {

    OrderService orderService;

    // Tạo đơn hàng mới
    @PostMapping
    public ApiResponse<String> createOrder(@RequestBody @Valid OrderRequest request) {
        orderService.createOrder(request);
        return ApiResponse.<String>builder()
                .data("Order created successfully")
                .build();
    }

    // Lấy đơn hàng theo orderId
    @GetMapping("/{orderId}")
    public ApiResponse<OrderResponse> getOrderById(@PathVariable String orderId) {
        return ApiResponse.<OrderResponse>builder()
                .data(orderService.getOrderById(orderId))
                .build();
    }

    // Lấy tất cả đơn hàng của một user
    @GetMapping("/user/{userId}")
    public ApiResponse<List<OrderResponse>> getOrdersByUserId(@PathVariable String userId) {
        return ApiResponse.<List<OrderResponse>>builder()
                .data(orderService.getOrdersByUserId(userId))
                .build();
    }

    // Cập nhật trạng thái đơn hàng
    @PostMapping("/{orderId}/status")
    public ApiResponse<String> updateOrderStatus(@PathVariable String orderId, @RequestBody UpdateOrderStatusRequest request) {
        orderService.updateOrderStatus(orderId, OrderStatus.valueOf(request.getStatus().toUpperCase()));
        return ApiResponse.<String>builder()
                .data("Order status updated successfully")
                .build();
    }

    // Xoá đơn hàng
    @DeleteMapping("/{orderId}")
    public ApiResponse<String> deleteOrder(@PathVariable String orderId) {
        orderService.deleteOrder(orderId);
        return ApiResponse.<String>builder()
                .data("Order deleted successfully")
                .build();
    }
}
