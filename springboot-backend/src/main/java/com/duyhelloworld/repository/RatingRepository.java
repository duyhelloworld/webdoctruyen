package com.duyhelloworld.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.duyhelloworld.entity.Rating;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Integer>{
    
}
