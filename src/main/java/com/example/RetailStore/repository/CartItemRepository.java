package com.example.RetailStore.repository;

import com.example.RetailStore.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, String> {

    @Query("""
            select c from CartItem c where 
            c.product.productId = :productId
            """)
    Optional<CartItem> findCartItem(@Param("productId") String productId);
}
