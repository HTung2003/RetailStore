package com.example.RetailStore.service;


import com.example.RetailStore.dto.request.AddQuantityRequest;
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
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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

    @Transactional
    public void createCart(CartRequest cartRequest) {
        User user = userRepository.findById(cartRequest.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Cart cart = cartRepository.findByUserUserId(cartRequest.getUserId()).orElseThrow(
                () -> new AppException(ErrorCode.CART_NOT_FOUND)
        );
        Product product = productRepository.findById(cartRequest.getItems().getProductId()).orElseThrow(
                () -> new AppException(ErrorCode.PRODUCT_NOT_FOUND)
        );
        CartItem cartItemOld = cartItemRepository.findCartItem(
                cartRequest.getItems().getProductId(),
                cartRequest.getUserId()).orElseThrow(
                () -> new AppException(ErrorCode.CART_ITEM_NOT_FOUND)
        );
        List<CartItem> cartItems = new ArrayList<>();
        if (Objects.nonNull(cartItemOld)) {
            cartItemOld.setQuantity(cartItemOld.getQuantity() + 1);
            cartItems.add(cartItemOld);
            cartItemRepository.save(cartItemOld);
        } else {
            CartItem cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setQuantity(cartRequest.getItems().getQuantity());
            cartItem.setProduct(product);
            cartItems.add(cartItem);
            cartItemRepository.save(cartItem);
        }

        cart.setItems(cartItems);
        cart.setUser(user);
        cartRepository.save(cart); // Lưu lại cart cùng các items
    }

    public void addQuantity(AddQuantityRequest request) {
        CartItem cartItem = cartItemRepository.findById(request.getCartIemId()).orElseThrow(
                () -> new AppException(ErrorCode.CART_ITEM_NOT_FOUND)
        );
        cartItem.setQuantity(request.getQuantity());
        cartItemRepository.save(cartItem);
    }

    public CartResponse getCartByUserId(String userId) {
        Cart cart = cartRepository.findByUserUserId(userId)
                .orElseThrow(() -> new AppException(ErrorCode.CART_NOT_FOUND));
        return modelMapper.map(cart, CartResponse.class);
    }

    @Transactional
    public void updateCart(String userId, UpdateCartRequest cartRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Cart cart = cartRepository.findByUserUserId(userId).orElseThrow(
                () -> new AppException(ErrorCode.CART_NOT_FOUND)
        );
        Product product = productRepository.findById(cartRequest.getItems().getProductId()).orElseThrow(
                () -> new AppException(ErrorCode.PRODUCT_NOT_FOUND)
        );
        CartItem cartItemOld = cartItemRepository.findCartItem(
                cartRequest.getItems().getProductId(),
                userId).orElseThrow(
                () -> new AppException(ErrorCode.CART_ITEM_NOT_FOUND)
        );
        List<CartItem> cartItems = new ArrayList<>();
        if (Objects.nonNull(cartItemOld)) {
            cartItemOld.setQuantity(cartItemOld.getQuantity() + 1);
            cartItems.add(cartItemOld);
            cartItemRepository.save(cartItemOld);
        } else {
            CartItem cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setQuantity(cartRequest.getItems().getQuantity());
            cartItem.setProduct(product);
            cartItems.add(cartItem);
            cartItemRepository.save(cartItem);
        }

        cart.setItems(cartItems);
        cart.setUser(user);
        cartRepository.save(cart); // Lưu lại cart cùng các items
    }

    @Transactional
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

    @Transactional
    public void clearCart(String userId) {
        Cart cart = cartRepository.findByUserUserId(userId)
                .orElseThrow(() -> new AppException(ErrorCode.CART_NOT_FOUND));
        cartItemRepository.deleteAll(cart.getItems());
        cart.getItems().clear();
        cartRepository.save(cart);
    }
}


