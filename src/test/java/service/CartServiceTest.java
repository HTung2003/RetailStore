package service;

import dto.request.CartItemRequest;
import dto.request.CartRequest;
import dto.response.CartResponse;
import entity.*;
import enums.Role;
import exception.AppException;
import exception.ErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import repository.CartItemRepository;
import repository.CartRepository;
import repository.ProductRepository;
import repository.UserRepository;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CartServiceTest {

    @Mock private CartRepository cartRepository;
    @Mock private ProductRepository productRepository;
    @Mock private UserRepository userRepository;
    @Mock private CartItemRepository cartItemRepository;
    @Mock private ModelMapper modelMapper;

    @InjectMocks
    private CartService cartService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createCart_success() {
        String userId = "user1";
        String productId = "prod1";

        CartItemRequest itemRequest = CartItemRequest.builder()
                .productId(productId)
                .quantity(2)
                .build();

        CartRequest cartRequest = CartRequest.builder()
                .userId(userId)
                .items(List.of(itemRequest))
                .build();

        User user = new User();
        user.setUserId(userId);
        Product product = new Product();
        product.setProductId(productId);

        Cart cart = new Cart();
        cart.setUser(user);
        cart.setItems(new ArrayList<>());

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        assertDoesNotThrow(() -> cartService.createCart(cartRequest));

        verify(cartRepository, times(2)).save(any(Cart.class));
        verify(productRepository).findById(productId);
        verify(userRepository).findById(userId);
    }

    @Test
    void getCartByUserId_success() {
        String userId = "user1";
        Cart cart = new Cart();
        CartResponse response = new CartResponse();

        when(cartRepository.findByUserUserId(userId)).thenReturn(Optional.of(cart));
        when(modelMapper.map(cart, CartResponse.class)).thenReturn(response);

        CartResponse result = cartService.getCartByUserId(userId);

        assertEquals(response, result);
    }

    @Test
    void updateCart_success() {
        String userId = "user1";
        String productId = "prod1";

        CartItemRequest itemRequest = CartItemRequest.builder()
                .productId(productId)
                .quantity(3)
                .build();

        CartRequest request = CartRequest.builder()
                .userId(userId)
                .items(List.of(itemRequest))
                .build();

        Product product = new Product();
        product.setProductId(productId);

        Cart cart = new Cart();
        cart.setItems(new ArrayList<>());

        when(cartRepository.findByUserUserId(userId)).thenReturn(Optional.of(cart));
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        cartService.updateCart(userId, request);

        verify(cartItemRepository).deleteAll(anyList());
        verify(cartRepository).save(cart);
    }

    @Test
    void removeItemFromCart_success() {
        String userId = "user1";
        String productId = "prod1";

        Product product = new Product();
        product.setProductId(productId);

        CartItem item = new CartItem();
        item.setProduct(product);

        Cart cart = new Cart();
        cart.setItems(new ArrayList<>(List.of(item)));

        when(cartRepository.findByUserUserId(userId)).thenReturn(Optional.of(cart));

        cartService.removeItemFromCart(userId, productId);

        verify(cartItemRepository).delete(item);
        verify(cartRepository).save(cart);
        assertTrue(cart.getItems().isEmpty());
    }

    @Test
    void clearCart_success() {
        String userId = "user1";

        CartItem item1 = new CartItem();
        CartItem item2 = new CartItem();

        Cart cart = new Cart();
        cart.setItems(new ArrayList<>(List.of(item1, item2)));

        when(cartRepository.findByUserUserId(userId)).thenReturn(Optional.of(cart));

        cartService.clearCart(userId);

        verify(cartItemRepository).deleteAll(cart.getItems());
        verify(cartRepository).save(cart);
        assertTrue(cart.getItems().isEmpty());
    }

    @Test
    void getCartByUserId_notFound() {
        when(cartRepository.findByUserUserId("notfound")).thenReturn(Optional.empty());
        AppException exception = assertThrows(AppException.class, () -> cartService.getCartByUserId("notfound"));
        assertEquals(ErrorCode.CART_NOT_FOUND, exception.getErrorCode());
    }
}
