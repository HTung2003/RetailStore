package com.example.RetailStore.repository;

import com.example.RetailStore.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {
    List<Order> findByUserUserId(String userId);

    @Query("SELECT o FROM Order o")
    Page<Order> searchOrder(Pageable pageable);
}
