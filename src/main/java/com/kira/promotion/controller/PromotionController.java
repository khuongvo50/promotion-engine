package com.kira.promotion.controller;

import com.kira.promotion.domain.PromotionContext;
import com.kira.promotion.domain.PromotionResult;
import com.kira.promotion.service.PromotionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/promotions")
@RequiredArgsConstructor
public class PromotionController {

    private final PromotionService promotionService;

    @PostMapping("/auto-suggest")
    public List<PromotionResult> autoSuggest(@RequestBody PromotionContext context) {
        return promotionService.autoSuggest(context);
    }
}