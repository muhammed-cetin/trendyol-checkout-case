package com.trendyol.domain.promotion;

import com.trendyol.domain.cart.Cart;
import com.trendyol.domain.item.DefaultItem;
import com.trendyol.domain.item.Item;
import com.trendyol.domain.item.VasItem;

public class CategoryPromotion implements Promotion {

    private static final int PROMOTION_ID = 5676;
    private static final int TARGET_CATEGORY_ID = 3003;
    private static final double DISCOUNT_RATE = 0.05; // %5

    @Override
    public int getPromotionId() {
        return PROMOTION_ID;
    }

    @Override
    public double calculateDiscount(Cart cart) {
        if (!isApplicable(cart)) {
            return 0.0;
        }

        double categoryItemsTotal = calculateCategoryItemsTotal(cart);
        return categoryItemsTotal * DISCOUNT_RATE;
    }

    @Override
    public boolean isApplicable(Cart cart) {
        return cart.getItems().stream()
                .anyMatch(item -> item.getCategoryId() == TARGET_CATEGORY_ID);
    }

    private double calculateCategoryItemsTotal(Cart cart) {
        double total = 0.0;

        for (Item item : cart.getItems()) {
            // Only calculate items in category 3003
            if (item.getCategoryId() == TARGET_CATEGORY_ID) {
                total += item.getPrice() * item.getQuantity();

                // Add DefaultItem's VasItems as well
                if (item instanceof DefaultItem defaultItem) {
                    for (VasItem vasItem : defaultItem.getVasItems()) {
                        total += vasItem.getPrice() * vasItem.getQuantity();
                    }
                }
            }
        }

        return total;
    }
}
