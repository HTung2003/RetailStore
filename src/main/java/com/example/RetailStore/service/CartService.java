package com.example.RetailStore.service;


import com.example.RetailStore.dto.request.CartItemRequest;
import com.example.RetailStore.dto.request.CartRequest;
import com.example.RetailStore.dto.request.UpdateCartRequest;
import com.example.RetailStore.dto.response.CartResponse;
import com.example.RetailStore.entity.Cart;
import com.example.RetailStore.entity.CartItem;
import com.example.RetailStore.entity.Product;
import com.example.RetailStore.entity.User;
import com.example.RetailStore.exception.AppException;
import com.example.RetailStore.exception.ErrorCode;
import com.example.RetailStore.repository.CartItemRepository;
import com.example.RetailStore.repository.CartRepository;
import com.example.RetailStore.repository.ProductRepository;
import com.example.RetailStore.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CartService {

    CartRepository cartRepository;
    ProductRepository productRepository;
    UserRepository userRepository;
    CartItemRepository cartItemRepository;
    ModelMapper modelMapper;

    public void createCart(CartRequest cartRequest) {
        User user = userRepository.findById(cartRequest.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Cart cart = new Cart();
        cart.setUser(user);
        cartRepository.save(cart); // Lúc này cart đã có id để liên kết với CartItem

        for (CartItemRequest itemReq : cartRequest.getItems()) {
            Product product = productRepository.findById(itemReq.getProductId())
                    .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

            CartItem item = new CartItem();
            item.setCart(cart); // thiết lập mối quan hệ 2 chiều
            item.setProduct(product);
            item.setQuantity(itemReq.getQuantity());

            cart.getItems().add(item); // ✅ Thêm vào collection hiện có thay vì setItems()
        }

        cartRepository.save(cart); // Lưu lại cart cùng các items
    }


    public CartResponse getCartByUserId(String userId) {
        Cart cart = cartRepository.findByUserUserId(userId)
                .orElseThrow(() -> new AppException(ErrorCode.CART_NOT_FOUND));
        return modelMapper.map(cart, CartResponse.class);
    }

    public void updateCart(String userId, UpdateCartRequest request) {
        Cart cart = cartRepository.findByUserUserId(userId)
                .orElseThrow(() -> new AppException(ErrorCode.CART_NOT_FOUND));


        // Thêm các item mới
        List<CartItem> newItems = request.getItems().stream().map(itemReq -> {
            Product product = productRepository.findById(itemReq.getProductId())
                    .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
            CartItem item = new CartItem();
            item.setProduct(product);
            item.setQuantity(itemReq.getQuantity());
            item.setCart(cart);
            return item;
        }).collect(Collectors.toList());

        cart.getItems().addAll(newItems);
        cartRepository.save(cart);
    }

    public void removeItemFromCart(String userId, String productId) {
        Cart cart = cartRepository.findByUserUserId(userId)
                .orElseThrow(() -> new AppException(ErrorCode.CART_NOT_FOUND));

        CartItem itemToRemove = cart.getItems().stream()
                .filter(item -> item.getProduct().getProductId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new AppException(ErrorCode.CART_ITEM_NOT_FOUND));

        cart.getItems().remove(itemToRemove);
        cartItemRepository.delete(itemToRemove);
        cartRepository.save(cart);
    }

    public void clearCart(String userId) {
        Cart cart = cartRepository.findByUserUserId(userId)
                .orElseThrow(() -> new AppException(ErrorCode.CART_NOT_FOUND));
        cartItemRepository.deleteAll(cart.getItems());
        cart.getItems().clear();
        cartRepository.save(cart);
    }
}


