package com.trendyol.domain.promotion;

import com.trendyol.domain.cart.Cart;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PromotionService {

    private final List<Promotion> promotions;

    public PromotionService() {
        this.promotions = new ArrayList<>();
        initializePromotions();
    }

    private void initializePromotions() {
        promotions.add(new SameSellerPromotion());
        promotions.add(new CategoryPromotion());
        promotions.add(new TotalPricePromotion());
    }

    public void applyBestPromotion(Cart cart) {
        Promotion bestPromotion = findBestPromotion(cart);

        if (bestPromotion != null) {
            double discount = bestPromotion.calculateDiscount(cart);
            cart.setTotalDiscount(discount);
            cart.setAppliedPromotionId(bestPromotion.getPromotionId());
        } else {
            // No promotions applied
            cart.setTotalDiscount(0.0);
            cart.setAppliedPromotionId(0);
        }
    }

    private Promotion findBestPromotion(Cart cart) {
        return promotions.stream()
                .filter(promotion -> promotion.isApplicable(cart))
                .max(Comparator.comparingDouble(promotion -> promotion.calculateDiscount(cart)))
                .orElse(null);
    }
}
