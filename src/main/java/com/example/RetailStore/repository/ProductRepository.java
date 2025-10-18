package com.example.RetailStore.repository;

import com.example.RetailStore.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {

    @Query("""
                SELECT u FROM Product u
                WHERE (:keyword IS NULL OR :keyword = '' OR LOWER(u.name) LIKE LOWER(CONCAT('%', :keyword, '%')))
            """)
    Page<Product> searchByName(@Param("keyword") String keyword, Pageable pageable);

}
