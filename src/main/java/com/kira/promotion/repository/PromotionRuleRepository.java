package com.kira.promotion.repository;

import com.kira.promotion.entity.PromotionRule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PromotionRuleRepository extends JpaRepository<PromotionRule, Long> {
    List<PromotionRule> findByActiveTrue();
}
