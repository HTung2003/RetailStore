package com.example.RetailStore.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "profit_managerments")
public class ProfitManagement {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "total_amount")
    private BigDecimal totalAmount;

    private LocalDateTime createdDate;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
}
