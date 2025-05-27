package com.kira.promotion.engine.handler;

import com.kira.promotion.domain.PromotionContext;
import com.kira.promotion.domain.PromotionResult;
import com.kira.promotion.entity.PromotionRule;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;

@Component("REDEEM_POINT")
public class RedeemPointHandler implements PromotionRuleHandler {

    @Override
    public PromotionResult apply(PromotionContext context, PromotionRule rule) {
        Map<String, Object> params = rule.getParams();

        int usedPoint = Math.min(context.getAvailablePoints(),
                Integer.parseInt(params.getOrDefault("maxPoint", 0).toString()));
        int rate = Integer.parseInt(params.getOrDefault("pointToCashRate", 100).toString());

        BigDecimal discount = new BigDecimal(usedPoint).divide(new BigDecimal(rate));

        return new PromotionResult(
                rule.getCode(),
                discount,
                BigDecimal.ZERO,
                0,
                usedPoint,
                "Dùng " + usedPoint + " xu để giảm " + discount + " theo rule " + rule.getCode()
        );
    }
}
