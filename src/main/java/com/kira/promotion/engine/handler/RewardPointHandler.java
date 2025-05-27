package com.kira.promotion.engine.handler;

import com.kira.promotion.domain.PromotionContext;
import com.kira.promotion.domain.PromotionResult;
import com.kira.promotion.entity.PromotionRule;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;

@Component("REWARD_POINT")
public class RewardPointHandler implements PromotionRuleHandler {

    @Override
    public PromotionResult apply(PromotionContext context, PromotionRule rule) {
        Map<String, Object> params = rule.getParams();
        int rewardPoint = Integer.parseInt(params.getOrDefault("point", 0).toString());

        return new PromotionResult(
                rule.getCode(),
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                rewardPoint,
                0,
                "Tặng " + rewardPoint + " xu từ rule " + rule.getCode()
        );
    }
}
