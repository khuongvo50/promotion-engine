package com.kira.promotion.engine.resolver;

import com.kira.promotion.domain.PromotionResult;
import com.kira.promotion.entity.PromotionRule;

import java.util.*;
import java.util.stream.Collectors;

public class PromotionConflictResolver {

    public static List<PromotionResult> resolve(List<PromotionRule> rules, List<PromotionResult> results) {
        // Map campaignId -> campaignPriority, stackable
        Map<Long, CampaignMeta> campaignMeta = rules.stream()
                .filter(r -> r.getCampaign() != null)
                .collect(Collectors.toMap(
                        r -> r.getCampaign().getId(),
                        r -> new CampaignMeta(r.getCampaign().getPriority(), r.getCampaign().isStackable()),
                        (r1, r2) -> r1 // same campaign, no problem
                ));

        // Group result by campaignId
        Map<Long, List<PromotionResult>> grouped = new HashMap<>();
        for (int i = 0; i < rules.size(); i++) {
            PromotionRule rule = rules.get(i);
            PromotionResult result = results.get(i);
            if (result == null || rule.getCampaign() == null) continue;

            grouped.computeIfAbsent(rule.getCampaign().getId(), x -> new ArrayList<>()).add(result);
        }

        // Sort campaigns by priority desc
        List<Map.Entry<Long, CampaignMeta>> sortedCampaigns = new ArrayList<>(campaignMeta.entrySet());
        sortedCampaigns.sort((a, b) -> Integer.compare(b.getValue().priority, a.getValue().priority));

        List<PromotionResult> finalResults = new ArrayList<>();

        for (Map.Entry<Long, CampaignMeta> entry : sortedCampaigns) {
            List<PromotionResult> group = grouped.get(entry.getKey());
            if (group == null || group.isEmpty()) continue;

            if (entry.getValue().stackable) {
                finalResults.addAll(group);
            } else {
                // nếu không stack được → chỉ chọn 1 rule đầu tiên
                finalResults.add(group.get(0));
                break; // dừng sau campaign đầu tiên không stack
            }
        }

        return finalResults;
    }

    private record CampaignMeta(int priority, boolean stackable) {}
}
