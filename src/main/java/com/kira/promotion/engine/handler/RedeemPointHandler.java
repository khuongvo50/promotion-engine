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

        int maxPoint = Integer.parseInt(params.getOrDefault("maxPoint", 0).toString());
        int ratio = Integer.parseInt(params.getOrDefault("pointToCashRate", 100).toString()); // 100 xu = 1k

        int usablePoint = Math.min(context.getAvailablePoints(), maxPoint);
        BigDecimal discount = new BigDecimal(usablePoint).divide(new BigDecimal(ratio));

        if (usablePoint > 0) {
            return new PromotionResult(
                    rule.getCode(),
                    discount,
                    BigDecimal.ZERO,
                    0,
                    usablePoint,
                    "Dùng " + usablePoint + " xu để giảm " + discount + " đ"
            );
        }

        return null;
    }
}
