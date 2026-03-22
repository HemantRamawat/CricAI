package com.AI.cricket.cricket.repository;

import com.AI.cricket.cricket.entity.MatchPrediction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchPredictionRepository
        extends JpaRepository<MatchPrediction,Long> {
}