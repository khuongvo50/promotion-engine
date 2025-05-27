package com.kira.promotion.engine.resolver;

import com.kira.promotion.domain.PromotionResult;
import com.kira.promotion.engine.model.RuleResultPair;
import com.kira.promotion.entity.PromotionRule;

import java.util.*;

public class PromotionConflictResolver {

    public static List<RuleResultPair> resolveToPairs(List<RuleResultPair> input) {
        Map<Long, CampaignMeta> campaignMeta = new HashMap<>();
        Map<Long, List<RuleResultPair>> grouped = new HashMap<>();

        for (RuleResultPair pair : input) {
            PromotionRule rule = pair.rule();
            PromotionResult result = pair.result();

            if (result == null || rule.getCampaign() == null) continue;

            long campaignId = rule.getCampaign().getId();

            campaignMeta.putIfAbsent(campaignId,
                    new CampaignMeta(rule.getCampaign().getPriority(), rule.getCampaign().isStackable()));

            grouped.computeIfAbsent(campaignId, x -> new ArrayList<>()).add(pair);
        }

        // Sort campaign by priority desc
        List<Map.Entry<Long, CampaignMeta>> sorted = new ArrayList<>(campaignMeta.entrySet());
        sorted.sort((a, b) -> Integer.compare(b.getValue().priority, a.getValue().priority));

        List<RuleResultPair> finalResults = new ArrayList<>();

        for (Map.Entry<Long, CampaignMeta> entry : sorted) {
            List<RuleResultPair> group = grouped.get(entry.getKey());
            if (group == null || group.isEmpty()) continue;

            if (entry.getValue().stackable) {
                finalResults.addAll(group);
            } else {
                finalResults.add(group.get(0));
                break; // Không stack được → dừng luôn
            }
        }

        return finalResults;
    }


    private record CampaignMeta(int priority, boolean stackable) {
    }
}
