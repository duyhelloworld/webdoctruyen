package com.duyhelloworld.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.duyhelloworld.entity.TokenDied;

public interface TokenDiedRepository extends JpaRepository<TokenDied, Long>{
    public boolean existsByToken(String token);
}
