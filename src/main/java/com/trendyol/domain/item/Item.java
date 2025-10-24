package com.trendyol.domain.item;


/*
 * There is nothing concrete in place of Item, just the layout. DefaultItem, DigitalItem, VasItem all derive from Item.
 */

public abstract class Item {

    /// Allow subclasses (DefaultItem, DigitalItem) to be accessed but not externally (encapsulation) [protected]
    protected int itemId;
    protected int categoryId;
    protected int sellerId;
    protected double price;
    protected int quantity;

    public Item(int itemId, int categoryId, int sellerId, double price, int quantity) {
        this.itemId = itemId;
        this.categoryId = categoryId;
        this.sellerId = sellerId;
        this.price = price;
        this.quantity = quantity;
    }

    ///  When the same product is added to the cart again, its quantity is increased.
    public void increaseQuantity(int amount) {
        this.quantity += amount;
    }

    public void decreaseQuantity(int amount) {
        if (this.quantity - amount < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }
        this.quantity -= amount;
    }

    public int getItemId() {
        return itemId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public int getSellerId() {
        return sellerId;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }
}
