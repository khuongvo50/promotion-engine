package com.kira.promotion.engine;

import com.kira.promotion.domain.PromotionContext;
import com.kira.promotion.domain.PromotionResult;
import com.kira.promotion.engine.handler.PromotionRuleHandler;
import com.kira.promotion.engine.resolver.PromotionConflictResolver;
import com.kira.promotion.entity.PromotionRule;
import com.kira.promotion.repository.PromotionRuleRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
public class PromotionEngine {

    private final PromotionRuleRepository ruleRepository;
    private final ApplicationContext applicationContext;

    // map handler theo enum name (vd: FIXED_AMOUNT -> FixedAmountHandler)
    private final Map<String, PromotionRuleHandler> handlerMap = new HashMap<>();

    @PostConstruct
    public void init() {
        Map<String, PromotionRuleHandler> beans = applicationContext.getBeansOfType(PromotionRuleHandler.class);
        handlerMap.putAll(beans);
    }

    public List<PromotionResult> evaluate(PromotionContext context) {
        List<PromotionRule> rules = ruleRepository.findByActiveTrue();

        List<PromotionResult> rawResults = new ArrayList<>();

        for (PromotionRule rule : rules) {
            PromotionRuleHandler handler = handlerMap.get(rule.getType().name());
            if (handler == null) continue;

            PromotionResult result = handler.apply(context, rule);
            rawResults.add(result);
        }

        return PromotionConflictResolver.resolve(rules, rawResults);
    }
}