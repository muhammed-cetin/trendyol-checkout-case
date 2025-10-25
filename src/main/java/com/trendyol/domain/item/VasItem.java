package com.trendyol.domain.item;

import com.trendyol.domain.exception.ItemException;

public class VasItem extends Item {

    public static final int VAS_CATEGORY_ID = 3242;
    public static final int VAS_SELLER_ID = 5003;
    private static final int MAX_VAS_QUANTITY = 3;

    public VasItem(int itemId, int categoryId, int sellerId, double price, int quantity) {
        super(itemId, categoryId, sellerId, price, quantity);
        validate(categoryId, sellerId, quantity);
    }

    private void validate(int categoryId, int sellerId, int quantity) {
        // CategoryID check
        if (categoryId != VAS_CATEGORY_ID) {
            throw new ItemException(
                    "VasItem categoryId must be " + VAS_CATEGORY_ID
            );
        }

        // SellerID check
        if (sellerId != VAS_SELLER_ID) {
            throw new ItemException(
                    "VasItem sellerId must be " + VAS_SELLER_ID
            );
        }

        // Quantity check
        if (quantity > MAX_VAS_QUANTITY) {
            throw new ItemException(
                    "VasItem quantity cannot exceed " + MAX_VAS_QUANTITY
            );
        }
    }

    // Getter (for vasItemId)
    public int getVasItemId() {
        return this.itemId;
    }
}
