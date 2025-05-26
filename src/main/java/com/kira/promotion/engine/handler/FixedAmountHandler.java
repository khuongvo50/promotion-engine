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

        BigDecimal minSpend = new BigDecimal(params.getOrDefault("minSpend", 0).toString());
        BigDecimal amount = new BigDecimal(params.getOrDefault("amount", 0).toString());

        if (context.getTotalAmount().compareTo(minSpend) >= 0) {
            return new PromotionResult(
                    rule.getCode(),
                    amount, // discount
                    BigDecimal.ZERO, // shipping
                    0, 0,
                    "Giảm " + amount + " cho đơn từ " + minSpend
            );
        }

        return null;
    }
}
