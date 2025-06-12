package com.example.RetailStore.repository;

import com.example.RetailStore.entity.InvalidatedToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvalidatedTokenRepository extends JpaRepository<InvalidatedToken,String> {
    boolean existsById(String tokenid);
}
