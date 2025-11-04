package com.example.RetailStore.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class ProfitManagementResponse {
    private String id;

    private LocalDateTime createdDate;

    private BigDecimal totalAmount;

    private OrderResponse order;
}
