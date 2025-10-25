package com.trendyol.domain.promotion;

import com.trendyol.domain.cart.Cart;

public interface Promotion {

    int getPromotionId();

    double calculateDiscount(Cart cart);

    boolean isApplicable(Cart cart);
}
