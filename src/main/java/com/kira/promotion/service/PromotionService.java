package com.kira.promotion.service;

import com.kira.promotion.domain.PromotionContext;
import com.kira.promotion.domain.PromotionResult;
import com.kira.promotion.engine.PromotionEngine;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PromotionService {

    private final PromotionEngine promotionEngine;

    public List<PromotionResult> applyPromotions(PromotionContext context) {
        return promotionEngine.evaluate(context);
    }
}
