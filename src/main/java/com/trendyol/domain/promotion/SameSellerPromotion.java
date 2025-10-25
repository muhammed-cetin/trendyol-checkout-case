package com.trendyol.domain.promotion;

import com.trendyol.domain.cart.Cart;
import com.trendyol.domain.item.Item;
import com.trendyol.domain.item.VasItem;

import java.util.HashSet;
import java.util.Set;

public class SameSellerPromotion implements Promotion {

    private static final int PROMOTION_ID = 9909;
    private static final double DISCOUNT_RATE = 0.10;


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
        return totalPrice * DISCOUNT_RATE;
    }

    @Override
    public boolean isApplicable(Cart cart) {
        Set<Integer> sellerIds = new HashSet<>();

        for (Item item : cart.getItems()) {
            if (!(item instanceof VasItem)) {
                sellerIds.add(item.getSellerId());
            }
        }

        return sellerIds.size() == 1;
    }
}
