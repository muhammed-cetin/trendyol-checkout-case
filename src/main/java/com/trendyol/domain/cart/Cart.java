package com.trendyol.domain.cart;

import com.trendyol.domain.exception.CartException;
import com.trendyol.domain.item.DefaultItem;
import com.trendyol.domain.item.Item;
import com.trendyol.domain.item.VasItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Cart {

    private static final int MAX_UNIQUE_ITEMS = 10;
    private static final int MAX_TOTAL_ITEMS = 30;
    private static final double MAX_TOTAL_AMOUNT = 500_000.0;

    private final List<Item> items;
    private int appliedPromotionId;
    private double totalDiscount;

    public Cart() {
        this.items = new ArrayList<>();
        this.appliedPromotionId = 0;
        this.totalDiscount = 0.0;
    }

    // Item add
    public void addItem(Item item) {
        validateBeforeAddingItem(item);

        Optional<Item> existingItem = findItemById(item.getItemId());

        if (existingItem.isPresent()) {
            existingItem.get().increaseQuantity(item.getQuantity());
        } else {
            items.add(item);
        }
    }

    // VasItem add (DefaultItem)
    public void addVasItemToItem(int itemId, VasItem vasItem) {
        Item item = findItemById(itemId)
                .orElseThrow(() -> new CartException("Item not found in cart: " + itemId));

        if (!(item instanceof DefaultItem defaultItem)) {
            throw new CartException("VasItem can only be added to DefaultItem");
        }

        defaultItem.addVasItem(vasItem);
    }

    // Item delete
    public boolean removeItem(int itemId) {
        return items.removeIf(item -> item.getItemId() == itemId);
    }

    // Reset cart
    public void reset() {
        items.clear();
        appliedPromotionId = 0;
        totalDiscount = 0.0;
    }

    // Validations
    private void validateBeforeAddingItem(Item item) {
        // 1. Check the number of unique items (except VasItem)
        if (!isItemExist(item.getItemId()) && getUniqueItemCount() >= MAX_UNIQUE_ITEMS) {
            throw new CartException(
                    "Cart cannot contain more than " + MAX_UNIQUE_ITEMS + " unique items"
            );
        }

        // 2. Total item count check
        int currentTotal = getTotalItemCount();
        if (currentTotal + item.getQuantity() > MAX_TOTAL_ITEMS) {
            throw new CartException(
                    "Total item count cannot exceed " + MAX_TOTAL_ITEMS
            );
        }

        // 3. Total amount control
        double itemTotalPrice = item.getPrice() * item.getQuantity();
        double potentialTotalAmount = calculateTotalPrice() + itemTotalPrice;

        if (potentialTotalAmount > MAX_TOTAL_AMOUNT) {
            throw new CartException(
                    "Cart total amount cannot exceed " + MAX_TOTAL_AMOUNT + " TL"
            );
        }
    }

    // Helper methods
    private Optional<Item> findItemById(int itemId) {
        return items.stream()
                .filter(item -> item.getItemId() == itemId)
                .findFirst();
    }

    private boolean isItemExist(int itemId) {
        return findItemById(itemId).isPresent();
    }

    private int getUniqueItemCount() {
        return items.size();
    }

    private int getTotalItemCount() {
        int total = 0;
        for (Item item : items) {
            total += item.getQuantity();

            // Count DefaultItem's VasItems too
            if (item instanceof DefaultItem defaultItem) {
                for (VasItem vasItem : defaultItem.getVasItems()) {
                    total += vasItem.getQuantity();
                }
            }
        }
        return total;
    }

    // Calculations
    public double calculateTotalPrice() {
        double total = 0.0;

        for (Item item : items) {
            // Item price
            total += item.getPrice() * item.getQuantity();

            // Prices of DefaultItem's VasItems
            if (item instanceof DefaultItem defaultItem) {
                for (VasItem vasItem : defaultItem.getVasItems()) {
                    total += vasItem.getPrice() * vasItem.getQuantity();
                }
            }
        }

        return total;
    }

    public double getTotalAmount() {
        return calculateTotalPrice() - totalDiscount;
    }

    // Getters
    public List<Item> getItems() {
        return new ArrayList<>(items); // Defensive copy
    }

    public int getAppliedPromotionId() {
        return appliedPromotionId;
    }

    public void setAppliedPromotionId(int appliedPromotionId) {
        this.appliedPromotionId = appliedPromotionId;
    }

    public double getTotalDiscount() {
        return totalDiscount;
    }

    public void setTotalDiscount(double totalDiscount) {
        this.totalDiscount = totalDiscount;
    }
}
