package service;

import dto.request.CartItemRequest;
import dto.request.OrderRequest;
import dto.response.OrderResponse;
import entity.Order;
import entity.Product;
import entity.User;
import enums.OrderStatus;
import exception.AppException;
import exception.ErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.modelmapper.ModelMapper;
import repository.OrderItemRepository;
import repository.OrderRepository;
import repository.ProductRepository;
import repository.UserRepository;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateOrderSuccess() {
        String userId = "user1";
        OrderRequest request = new OrderRequest();
        request.setUserId(userId);
        request.setShippingAddress("Address 123");
        // giả lập item request
        CartItemRequest itemReq1 = new CartItemRequest();
        itemReq1.setProductId("cartItem1");
        itemReq1.setQuantity(2);
        request.setItems(List.of(itemReq1));

        User user = new User();
        Product product = new Product();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(productRepository.findById("prod1")).thenReturn(Optional.of(product));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        orderService.createOrder(request);

        verify(userRepository).findById(userId);
        verify(productRepository).findById("prod1");
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void testCreateOrderFailUserNotExist() {
        String userId = "notExist";
        OrderRequest request = new OrderRequest();
        request.setUserId(userId);
        request.setItems(Collections.emptyList());

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        AppException ex = assertThrows(AppException.class, () -> orderService.createOrder(request));
        assertEquals(ErrorCode.USER_NOT_EXISTED, ex.getErrorCode());

        verify(userRepository).findById(userId);
        verifyNoMoreInteractions(productRepository);
        verifyNoMoreInteractions(orderRepository);
    }

    @Test
    void testCreateOrderFailProductNotFound() {
        String userId = "user1";
        OrderRequest request = new OrderRequest();
        request.setUserId(userId);
        CartItemRequest itemReq1 = new CartItemRequest();
        itemReq1.setProductId("cartItem1");
        itemReq1.setQuantity(2);
        request.setItems(List.of(itemReq1));

        User user = new User();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(productRepository.findById("prodNotExist")).thenReturn(Optional.empty());

        AppException ex = assertThrows(AppException.class, () -> orderService.createOrder(request));
        assertEquals(ErrorCode.PRODUCT_NOT_FOUND, ex.getErrorCode());

        verify(userRepository).findById(userId);
        verify(productRepository).findById("prodNotExist");
        verifyNoMoreInteractions(orderRepository);
    }

    @Test
    void testGetOrderByIdSuccess() {
        String orderId = "order1";
        Order order = new Order();
        OrderResponse response = new OrderResponse();

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(modelMapper.map(order, OrderResponse.class)).thenReturn(response);

        OrderResponse result = orderService.getOrderById(orderId);

        assertEquals(response, result);
        verify(orderRepository).findById(orderId);
        verify(modelMapper).map(order, OrderResponse.class);
    }

    @Test
    void testGetOrderByIdFailNotFound() {
        String orderId = "notExist";

        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        AppException ex = assertThrows(AppException.class, () -> orderService.getOrderById(orderId));
        assertEquals(ErrorCode.ORDER_NOT_FOUND, ex.getErrorCode());

        verify(orderRepository).findById(orderId);
        verifyNoInteractions(modelMapper);
    }

    @Test
    void testGetOrdersByUserId() {
        String userId = "user1";
        Order order1 = new Order();
        Order order2 = new Order();
        List<Order> orders = List.of(order1, order2);
        OrderResponse response1 = new OrderResponse();
        OrderResponse response2 = new OrderResponse();

        when(orderRepository.findByUserUserId(userId)).thenReturn(orders);
        when(modelMapper.map(order1, OrderResponse.class)).thenReturn(response1);
        when(modelMapper.map(order2, OrderResponse.class)).thenReturn(response2);

        List<OrderResponse> results = orderService.getOrdersByUserId(userId);

        assertEquals(2, results.size());
        assertTrue(results.contains(response1));
        assertTrue(results.contains(response2));
        verify(orderRepository).findByUserUserId(userId);
    }

    @Test
    void testUpdateOrderStatusSuccess() {
        String orderId = "order1";
        Order order = new Order();

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderRepository.save(order)).thenReturn(order);

        orderService.updateOrderStatus(orderId, OrderStatus.SHIPPED);

        assertEquals(OrderStatus.SHIPPED, order.getStatus());
        verify(orderRepository).findById(orderId);
        verify(orderRepository).save(order);
    }

    @Test
    void testUpdateOrderStatusFailNotFound() {
        String orderId = "notExist";

        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        AppException ex = assertThrows(AppException.class, () -> orderService.updateOrderStatus(orderId, OrderStatus.SHIPPED));
        assertEquals(ErrorCode.ORDER_NOT_FOUND, ex.getErrorCode());

        verify(orderRepository).findById(orderId);
        verifyNoMoreInteractions(orderRepository);
    }

    @Test
    void testDeleteOrderSuccess() {
        String orderId = "order1";
        Order order = new Order();

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        doNothing().when(orderRepository).delete(order);

        orderService.deleteOrder(orderId);

        verify(orderRepository).findById(orderId);
        verify(orderRepository).delete(order);
    }

    @Test
    void testDeleteOrderFailNotFound() {
        String orderId = "notExist";

        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        AppException ex = assertThrows(AppException.class, () -> orderService.deleteOrder(orderId));
        assertEquals(ErrorCode.ORDER_NOT_FOUND, ex.getErrorCode());

        verify(orderRepository).findById(orderId);
        verifyNoMoreInteractions(orderRepository);
    }
}
