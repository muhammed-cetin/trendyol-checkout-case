package com.trendyol.service;

import com.trendyol.domain.cart.Cart;
import com.trendyol.domain.item.DefaultItem;
import com.trendyol.domain.item.DigitalItem;
import com.trendyol.domain.item.Item;
import com.trendyol.domain.item.VasItem;
import com.trendyol.domain.promotion.PromotionService;

public class CartService {

    private static final int DIGITAL_CATEGORY_ID = 7889;

    private final Cart cart;
    private final PromotionService promotionService;

    public CartService() {
        this.cart = new Cart();
        this.promotionService = new PromotionService();
    }

    public boolean addItem(int itemId, int categoryId, int sellerId, double price, int quantity) {
        Item item = createItem(itemId, categoryId, sellerId, price, quantity);
        cart.addItem(item);
        applyPromotions();
        return true;
    }

    public boolean addVasItemToItem(int itemId, int vasItemId, int vasCategoryId, int vasSellerId, double price, int quantity) {
        VasItem vasItem = new VasItem(vasItemId, vasCategoryId, vasSellerId, price, quantity);
        cart.addVasItemToItem(itemId, vasItem);
        applyPromotions();
        return true;
    }

    public boolean removeItem(int itemId) {
        boolean removed = cart.removeItem(itemId);
        if (removed) {
            applyPromotions();
        }
        return removed;
    }

    public boolean resetCart() {
        cart.reset();
        return true;
    }

    public Cart getCart() {
        return cart;
    }

    private Item createItem(int itemId, int categoryId, int sellerId, double price, int quantity) {
        if (categoryId == DIGITAL_CATEGORY_ID) {
            return new DigitalItem(itemId, categoryId, sellerId, price, quantity);
        } else {
            return new DefaultItem(itemId, categoryId, sellerId, price, quantity);
        }
    }

    private void applyPromotions() {
        promotionService.applyBestPromotion(cart);
    }
}
