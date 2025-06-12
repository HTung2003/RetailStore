package com.example.RetailStore.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "product_id", nullable = false, updatable = false)
    String productId;

    @Column(nullable = false)
    String name;

    String description;

    @Column(nullable = false)
    Double price;

    String imageUrl;

    @Column(nullable = false)
    Integer stock;

}
