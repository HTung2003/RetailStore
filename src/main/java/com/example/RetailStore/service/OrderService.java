package com.example.RetailStore.service;


import com.example.RetailStore.dto.request.OrderRequest;
import com.example.RetailStore.dto.response.OrderResponse;
import com.example.RetailStore.dto.response.ProductResponse;
import com.example.RetailStore.entity.Order;
import com.example.RetailStore.entity.OrderItem;
import com.example.RetailStore.entity.Product;
import com.example.RetailStore.entity.User;
import com.example.RetailStore.enums.OrderStatus;
import com.example.RetailStore.exception.AppException;
import com.example.RetailStore.exception.ErrorCode;
import com.example.RetailStore.repository.OrderItemRepository;
import com.example.RetailStore.repository.OrderRepository;
import com.example.RetailStore.repository.ProductRepository;
import com.example.RetailStore.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderService {
    OrderRepository orderRepository;
    OrderItemRepository orderItemRepository;
    UserRepository userRepository;
    ProductRepository productRepository;
    ModelMapper modelMapper;

    public void createOrder(OrderRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Order order = new Order();
        order.setUser(user);
        order.setShippingAddress(request.getShippingAddress());
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);

        List<OrderItem> items = request.getItems().stream()
                .map(itemRequest -> {
                    Product product = productRepository.findById(itemRequest.getProductId())
                            .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

                    OrderItem item = new OrderItem();
                    item.setOrder(order); // gắn order
                    item.setProduct(product);
                    item.setQuantity(itemRequest.getQuantity());

                    return item;
                }).collect(Collectors.toList());

        order.setOrderItems(items);
        orderRepository.save(order);
    }

    public OrderResponse getOrderById(String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
        return modelMapper.map(order, OrderResponse.class);
    }

    public List<OrderResponse> getOrdersByUserId(String userId) {
        List<Order> orders = orderRepository.findByUserUserId(userId);
        return orders.stream()
                .map(order -> modelMapper.map(order, OrderResponse.class))
                .collect(Collectors.toList());
    }

    public void updateOrderStatus(String orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
        order.setStatus(status);
        orderRepository.save(order);
    }

    public void deleteOrder(String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
        orderRepository.delete(order);
    }

    public Page<OrderResponse> searchOrder(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Order> orders = orderRepository.searchOrder(pageable);

        return orders.map(order -> modelMapper.map(order, OrderResponse.class));
    }
}
