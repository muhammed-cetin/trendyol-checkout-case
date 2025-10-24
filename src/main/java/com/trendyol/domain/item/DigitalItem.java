package com.trendyol.domain.item;

public class DigitalItem extends Item {

    private static final int DIGITAL_CATEGORY_ID = 7889;
    private static final int MAX_DIGITAL_QUANTITY = 5;

    public DigitalItem(int itemId, int categoryId, int sellerId, double price, int quantity) {
        super(itemId, categoryId, sellerId, price, quantity);
        validate(categoryId, quantity);
    }

    private void validate(int categoryId, int quantity) {
        // CategoryID check
        if (categoryId != DIGITAL_CATEGORY_ID) {
            throw new IllegalArgumentException(
                    "DigitalItem categoryId must be " + DIGITAL_CATEGORY_ID
            );
        }

        // Quantity check
        if (quantity > MAX_DIGITAL_QUANTITY) {
            throw new IllegalArgumentException(
                    "DigitalItem quantity cannot exceed " + MAX_DIGITAL_QUANTITY
            );
        }
    }
}
