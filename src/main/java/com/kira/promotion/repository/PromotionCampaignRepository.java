package com.kira.promotion.repository;

import com.kira.promotion.entity.PromotionCampaign;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PromotionCampaignRepository extends JpaRepository<PromotionCampaign, Long> {
}

