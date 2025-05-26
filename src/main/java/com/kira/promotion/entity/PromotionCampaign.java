package com.kira.promotion.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class PromotionCampaign {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;
    private String name;
    private boolean active;

    private int priority; // độ ưu tiên

    private boolean stackable; // có thể kết hợp với rule khác không

    private LocalDateTime startAt;
    private LocalDateTime endAt;
}
