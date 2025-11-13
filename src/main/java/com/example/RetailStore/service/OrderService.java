package com.example.RetailStore.service;


import com.example.RetailStore.dto.request.OrderRequest;
import com.example.RetailStore.dto.response.OrderResponse;
import com.example.RetailStore.dto.response.ProfitManagementResponse;
import com.example.RetailStore.dto.response.TotalAmountResponse;
import com.example.RetailStore.entity.*;
import com.example.RetailStore.enums.OrderStatus;
import com.example.RetailStore.exception.AppException;
import com.example.RetailStore.exception.ErrorCode;
import com.example.RetailStore.repository.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Random;
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
    ProfitManagementRepository profitManagermentRepository;

    public void createOrder(OrderRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Order order = new Order();
        order.setUser(user);
        order.setCode(generateCode());
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
        double totalAmount = 0;
        if (Objects.equals(status, OrderStatus.DELIVERED)) {
            List<OrderItem> orderItems = order.getOrderItems();
            for (OrderItem orderItem : orderItems) {
                totalAmount = totalAmount + (orderItem.getProduct().getPrice() * orderItem.getQuantity());
            }
        }
        ProfitManagement profitManagerment = new ProfitManagement();
        profitManagerment.setCreatedDate(LocalDateTime.now());
        profitManagerment.setOrder(order);
        profitManagerment.setTotalAmount(BigDecimal.valueOf(totalAmount));
        profitManagermentRepository.save(profitManagerment);
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

    @PostAuthorize("hasRole('ADMIN')")
    public Page<ProfitManagementResponse> searchProfitManagement(String username, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<ProfitManagement> profitManagements = profitManagermentRepository.searchByUserName(username,pageable);

        return profitManagements.map(profitManagement -> modelMapper.map(profitManagement, ProfitManagementResponse.class));
    }

    public TotalAmountResponse getTotalAmount(){
        return TotalAmountResponse.builder()
                .totalAmountAllPayments(profitManagermentRepository.getTotalAmountAllPayments())
                .build();
    }

    public String generateCode() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder code = new StringBuilder(8);

        for (int i = 0; i < 10; i++) {
            code.append(characters.charAt(random.nextInt(characters.length())));
        }

        return code.toString();
    }


}
