package com.kira.promotion.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PromotionResult {
    private String ruleCode;             // Mã rule áp dụng
    private BigDecimal discountAmount;   // Số tiền được giảm
    private BigDecimal shippingDiscount; // Giảm phí ship
    private int rewardPoint;             // Xu được cộng
    private int usedPoint;               // Xu bị trừ
    private String displayMessage;       // Hiển thị cho người dùng
}
