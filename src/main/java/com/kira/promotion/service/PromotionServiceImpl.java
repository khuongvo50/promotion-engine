package com.kira.promotion.service;

import com.kira.promotion.domain.PromotionContext;
import com.kira.promotion.domain.PromotionResult;
import com.kira.promotion.engine.PromotionEngine;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PromotionServiceImpl implements PromotionService {

    private final PromotionEngine promotionEngine;

    @Override
    public List<PromotionResult> autoSuggest(PromotionContext context) {
        return promotionEngine.autoSuggest(context);
    }
}
