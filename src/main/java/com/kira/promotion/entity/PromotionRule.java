package com.kira.promotion.entity;

import com.kira.promotion.domain.enums.PromotionType;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Map;

@Entity
@Data
public class PromotionRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;

    @Enumerated(EnumType.STRING)
    private PromotionType type;

    private boolean active;

    private String expression; // ví dụ: context.totalAmount >= #minSpend

    @Convert(converter = JsonToMapConverter.class)
    private Map<String, Object> params;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "campaign_id")
    private PromotionCampaign campaign;
}
