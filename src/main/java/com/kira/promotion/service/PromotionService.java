package com.kira.promotion.service;

import com.kira.promotion.domain.PromotionContext;
import com.kira.promotion.domain.PromotionResult;

import java.util.List;

public interface PromotionService {
    List<PromotionResult> autoSuggest(PromotionContext context);
}
