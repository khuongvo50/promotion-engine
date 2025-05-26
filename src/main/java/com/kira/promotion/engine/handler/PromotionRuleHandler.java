package com.kira.promotion.engine.handler;

import com.kira.promotion.domain.PromotionContext;
import com.kira.promotion.domain.PromotionResult;
import com.kira.promotion.entity.PromotionRule;

public interface PromotionRuleHandler {
    PromotionResult apply(PromotionContext context, PromotionRule rule);
}
