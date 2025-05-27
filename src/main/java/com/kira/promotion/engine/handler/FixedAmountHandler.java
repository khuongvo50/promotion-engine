package com.kira.promotion.engine.handler;

import com.kira.promotion.domain.PromotionContext;
import com.kira.promotion.domain.PromotionResult;
import com.kira.promotion.entity.PromotionRule;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;

@Component("FIXED_AMOUNT")
public class FixedAmountHandler implements PromotionRuleHandler {

    @Override
    public PromotionResult apply(PromotionContext context, PromotionRule rule) {
        Map<String, Object> params = rule.getParams();

        BigDecimal amount = new BigDecimal(params.getOrDefault("amount", 0).toString());

        return new PromotionResult(
                rule.getCode(),
                amount,
                BigDecimal.ZERO,
                0, 0,
                "Giảm " + amount + " theo rule " + rule.getCode()
        );
    }
}
