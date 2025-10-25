package com.trendyol.domain.promotion;

import com.trendyol.domain.cart.Cart;

public class TotalPricePromotion implements Promotion {

    private static final int PROMOTION_ID = 1232;

    // Price ranges and discounts
    private static final double TIER_1_MIN = 500.0;
    private static final double TIER_1_DISCOUNT = 250.0;

    private static final double TIER_2_MIN = 5000.0;
    private static final double TIER_2_DISCOUNT = 500.0;

    private static final double TIER_3_MIN = 10000.0;
    private static final double TIER_3_DISCOUNT = 1000.0;

    private static final double TIER_4_MIN = 50000.0;
    private static final double TIER_4_DISCOUNT = 2000.0;

    @Override
    public int getPromotionId() {
        return PROMOTION_ID;
    }

    @Override
    public double calculateDiscount(Cart cart) {
        if (!isApplicable(cart)) {
            return 0.0;
        }

        double totalPrice = cart.calculateTotalPrice();

        // Tier control (top-down)
        if (totalPrice >= TIER_4_MIN) {
            return TIER_4_DISCOUNT;
        } else if (totalPrice >= TIER_3_MIN) {
            return TIER_3_DISCOUNT;
        } else if (totalPrice >= TIER_2_MIN) {
            return TIER_2_DISCOUNT;
        } else if (totalPrice >= TIER_1_MIN) {
            return TIER_1_DISCOUNT;
        }

        return 0.0;
    }

    @Override
    public boolean isApplicable(Cart cart) {
        double totalPrice = cart.calculateTotalPrice();
        return totalPrice >= TIER_1_MIN;
    }
}
