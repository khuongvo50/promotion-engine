package com.kira.promotion.engine.model;

import com.kira.promotion.domain.PromotionResult;
import com.kira.promotion.entity.PromotionRule;

public record RuleResultPair(
        PromotionRule rule,
        PromotionResult result
) {}
