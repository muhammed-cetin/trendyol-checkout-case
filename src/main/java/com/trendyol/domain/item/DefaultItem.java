package com.trendyol.domain.item;

import java.util.ArrayList;
import java.util.List;

public class DefaultItem extends Item {

    private static final int MAX_ITEM_QUANTITY = 10;
    private static final int MAX_VAS_ITEM_COUNT = 3;
    private static final int FURNITURE_CATEGORY_ID = 1001;
    private static final int ELECTRONICS_CATEGORY_ID = 3004;

    private final List<VasItem> vasItems;

    public DefaultItem(int itemId, int categoryId, int sellerId, double price, int quantity) {
        super(itemId, categoryId, sellerId, price, quantity);
        this.vasItems = new ArrayList<>();
        validate(quantity);
    }

    private void validate(int quantity) {
        // Quantity check
        if (quantity > MAX_ITEM_QUANTITY) {
            throw new IllegalArgumentException(
                    "DefaultItem quantity cannot exceed " + MAX_ITEM_QUANTITY
            );
        }
    }

    // VasItem add
    public void addVasItem(VasItem vasItem) {
        validateVasItem(vasItem);
        vasItems.add(vasItem);
    }

    private void validateVasItem(VasItem vasItem) {
        // VasItem can only be added to Furniture and Electronics categories.
        if (this.categoryId != FURNITURE_CATEGORY_ID &&
                this.categoryId != ELECTRONICS_CATEGORY_ID) {
            throw new IllegalArgumentException(
                    "VasItem can only be added to Furniture (1001) or Electronics (3004) categories"
            );
        }

        // A maximum of 3 VasItems can be added
        if (vasItems.size() >= MAX_VAS_ITEM_COUNT) {
            throw new IllegalArgumentException(
                    "Cannot add more than " + MAX_VAS_ITEM_COUNT + " VasItems to a DefaultItem"
            );
        }

        // VasItem price cannot be higher than DefaultItem price
        if (vasItem.getPrice() > this.price) {
            throw new IllegalArgumentException(
                    "VasItem price cannot be higher than DefaultItem price"
            );
        }
    }

    // Get VasItem List
    public List<VasItem> getVasItems() {
        return new ArrayList<>(vasItems); // Defensive copy
    }

    public boolean hasVasItems() {
        return !vasItems.isEmpty();
    }

    public int getVasItemCount() {
        return vasItems.size();
    }
}
