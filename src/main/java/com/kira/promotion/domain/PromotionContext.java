package com.kira.promotion.domain;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class PromotionContext {
    private String customerId;
    private BigDecimal totalAmount;
    private BigDecimal shippingFee;
    private List<CartItem> items;
    private int availablePoints; // Xu hiện tại

    @Data
    public static class CartItem {
        private String productId;
        private int quantity;
    }
}
