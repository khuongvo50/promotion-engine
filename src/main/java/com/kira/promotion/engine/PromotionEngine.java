package com.kira.promotion.engine;

import com.kira.promotion.domain.PromotionContext;
import com.kira.promotion.domain.PromotionResult;
import com.kira.promotion.domain.enums.PromotionType;
import com.kira.promotion.engine.handler.PromotionRuleHandler;
import com.kira.promotion.engine.model.RuleResultPair;
import com.kira.promotion.engine.resolver.PromotionConflictResolver;
import com.kira.promotion.entity.PromotionRule;
import com.kira.promotion.repository.PromotionRuleRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

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
        handlerMap.putAll(applicationContext.getBeansOfType(PromotionRuleHandler.class));
    }

    public List<RuleResultPair> evaluateToPairs(PromotionContext context) {
        List<PromotionRule> rules = ruleRepository.findByActiveTrue();
        List<RuleResultPair> evaluatedPairs = new ArrayList<>();

        for (PromotionRule rule : rules) {
            if (!evaluateExpression(rule, context)) continue;

            PromotionRuleHandler handler = handlerMap.get(rule.getType().name());
            if (handler == null) continue;

            PromotionResult result = handler.apply(context, rule);
            if (result != null) {
                evaluatedPairs.add(new RuleResultPair(rule, result));
            }
        }
        return evaluatedPairs;
    }

    public List<PromotionResult> autoSuggest(PromotionContext context) {
        List<RuleResultPair> evaluatedPairs = evaluateToPairs(context);

        // Gọi resolver để xử campaign conflict
        List<RuleResultPair> resolvedByCampaign = PromotionConflictResolver.resolveToPairs(evaluatedPairs);

        // Chọn 1 rule tốt nhất cho mỗi type
        Map<PromotionType, RuleResultPair> bestByType = new HashMap<>();

        for (RuleResultPair pair : resolvedByCampaign) {
            PromotionType type = pair.rule().getType();
            RuleResultPair current = bestByType.get(type);

            if (current == null || isBetter(pair.result(), current.result(), type)) {
                bestByType.put(type, pair);
            }
        }

        return bestByType.values().stream().map(RuleResultPair::result).toList();
    }

    private boolean isBetter(PromotionResult a, PromotionResult b, PromotionType type) {
        return switch (type) {
            case FIXED_AMOUNT, PERCENTAGE, REDEEM_POINT -> a.getDiscountAmount().compareTo(b.getDiscountAmount()) > 0;
            case FREESHIP -> a.getShippingDiscount().compareTo(b.getShippingDiscount()) > 0;
            case REWARD_POINT -> a.getRewardPoint() > b.getRewardPoint();
        };
    }

    private boolean evaluateExpression(PromotionRule rule, PromotionContext context) {
        String expr = rule.getExpression();
        if (expr == null || expr.isBlank()) return true;

        try {
            ExpressionParser parser = new SpelExpressionParser();
            StandardEvaluationContext evalContext = new StandardEvaluationContext();
            //evalContext.setVariable("context", context);
            evalContext.setRootObject(context);
            // Inject thêm tất cả param làm biến (#minSpend, #city...)
            rule.getParams().forEach(evalContext::setVariable);

            Boolean result = parser.parseExpression(expr).getValue(evalContext, Boolean.class);
//            System.out.printf( result ? "✅" : "❌" +" Rule %s: [%s] → %s", rule.getCode(), expr, result);

            System.out.printf(
                    (result ? "✅" : "❌") + " Rule %s\n  Expression: %s\n  Params: %s\n  Context: %s\n  => Result: %s\n\n",
                    rule.getCode(),
                    expr,
                    rule.getParams(),
                    context,
                    result
            );
            return result;
        } catch (Exception e) {
            System.out.println("Eval error: " + rule.getCode() + " - " + e.getMessage());
            return false;
        }
    }
}