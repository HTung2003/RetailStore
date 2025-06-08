package controller;

import dto.request.CartRequest;
import dto.response.ApiResponse;
import dto.response.CartResponse;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import service.CartService;

@RestController
@RequestMapping("/carts")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CartController {

    CartService cartService;

    // Tạo giỏ hàng mới
    @PostMapping
    public ApiResponse<String> createCart(@RequestBody @Valid CartRequest cartRequest) {
        cartService.createCart(cartRequest);
        return ApiResponse.<String>builder()
                .data("Cart created successfully")
                .build();
    }

    // Lấy giỏ hàng của 1 user
    @GetMapping("/{userId}")
    public ApiResponse<CartResponse> getCartByUserId(@PathVariable String userId) {
        return ApiResponse.<CartResponse>builder()
                .data(cartService.getCartByUserId(userId))
                .build();
    }

    // Cập nhật giỏ hàng của user
    @PutMapping("/{userId}")
    public ApiResponse<String> updateCart(@PathVariable String userId, @RequestBody @Valid CartRequest request) {
        cartService.updateCart(userId, request);
        return ApiResponse.<String>builder()
                .data("Cart updated successfully")
                .build();
    }

    // Xoá 1 sản phẩm khỏi giỏ hàng
    @DeleteMapping("/{userId}/remove-item/{productId}")
    public ApiResponse<String> removeItemFromCart(@PathVariable String userId, @PathVariable String productId) {
        cartService.removeItemFromCart(userId, productId);
        return ApiResponse.<String>builder()
                .data("Item removed from cart successfully")
                .build();
    }

    // Xoá toàn bộ giỏ hàng
    @DeleteMapping("/{userId}/clear")
    public ApiResponse<String> clearCart(@PathVariable String userId) {
        cartService.clearCart(userId);
        return ApiResponse.<String>builder()
                .data("Cart cleared successfully")
                .build();
    }
}
