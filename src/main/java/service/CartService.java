package service;


import dto.request.CartItemRequest;
import dto.request.CartRequest;
import dto.response.CartResponse;
import entity.Cart;
import entity.CartItem;
import entity.Product;
import entity.User;
import exception.AppException;
import exception.ErrorCode;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import repository.CartItemRepository;
import repository.CartRepository;
import repository.ProductRepository;
import repository.UserRepository;

import java.util.ArrayList;
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
        cart = cartRepository.save(cart);

        List<CartItem> cartItems = new ArrayList<>();

        for (CartItemRequest itemReq : cartRequest.getItems()) {
            Product product = productRepository.findById(itemReq.getProductId())
                    .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

            CartItem item = new CartItem();
            item.setCart(cart);
            item.setProduct(product);
            item.setQuantity(itemReq.getQuantity());
            cartItems.add(item);
        }

        cart.setItems(cartItems);

        cartRepository.save(cart);
    }

    public CartResponse getCartByUserId(String userId) {
        Cart cart = cartRepository.findByUserUserId(userId)
                .orElseThrow(() -> new AppException(ErrorCode.CART_NOT_FOUND));
        return modelMapper.map(cart, CartResponse.class);
    }

    public void updateCart(String userId, CartRequest request) {
        Cart cart = cartRepository.findByUserUserId(userId)
                .orElseThrow(() -> new AppException(ErrorCode.CART_NOT_FOUND));

        // Xoá các item cũ
        cartItemRepository.deleteAll(cart.getItems());
        cart.getItems().clear();

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


