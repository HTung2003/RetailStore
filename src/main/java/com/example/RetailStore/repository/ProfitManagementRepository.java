package com.example.RetailStore.repository;

import com.example.RetailStore.entity.ProfitManagement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface ProfitManagementRepository extends JpaRepository<ProfitManagement, String> {
    @Query("""
            select p from ProfitManagement  p where 
            (:username is null or :username = '' or p.order.user.username = :username)
            """)
    Page<ProfitManagement> searchByUserName(@Param("username") String username, Pageable pageable);

    @Query("SELECT SUM(p.totalAmount) FROM ProfitManagement p")
    BigDecimal getTotalAmountAllPayments();
}
