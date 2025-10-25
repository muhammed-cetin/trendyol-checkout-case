package com.trendyol.domain.cart;

import com.trendyol.domain.exception.CartException;
import com.trendyol.domain.item.DefaultItem;
import com.trendyol.domain.item.DigitalItem;
import com.trendyol.domain.item.Item;
import com.trendyol.domain.item.VasItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CartTest {

    private Cart cart;

    @BeforeEach
    void setUp() {
        cart = new Cart();
    }

    // ========== HAPPY PATH TESTS ==========

    @Test
    void shouldAddItemSuccessfully() {
        // Given
        DefaultItem item = new DefaultItem(1, 1001, 500, 1000.0, 2);

        // When
        cart.addItem(item);

        // Then
        assertEquals(1, cart.getItems().size());
        assertEquals(2000.0, cart.calculateTotalPrice());
    }

    @Test
    void shouldIncreaseQuantityWhenSameItemAddedAgain() {
        // Given
        DefaultItem item1 = new DefaultItem(1, 1001, 500, 1000.0, 2);
        DefaultItem item2 = new DefaultItem(1, 1001, 500, 1000.0, 3);

        // When
        cart.addItem(item1);
        cart.addItem(item2);

        // Then
        assertEquals(1, cart.getItems().size());
        assertEquals(5, cart.getItems().getFirst().getQuantity());
        assertEquals(5000.0, cart.calculateTotalPrice());
    }

    // ========== BOUNDARY TESTS: MAX UNIQUE ITEMS (10) ==========

    @Test
    void shouldAllowExactly10UniqueItems() {
        // Given & When - Boundary value (exactly 10 unique items)
        for (int i = 1; i <= 10; i++) {
            cart.addItem(new DefaultItem(i, 1001, 500, 100.0, 1));
        }

        // Then
        assertEquals(10, cart.getItems().size());
    }

    @Test
    void shouldThrowExceptionWhenMaxUniqueItemsExceeded() {
        // Given - At boundary (10 unique items)
        for (int i = 1; i <= 10; i++) {
            cart.addItem(new DefaultItem(i, 1001, 500, 100.0, 1));
        }

        // When & Then - Exceeding boundary (11th item)
        DefaultItem item11 = new DefaultItem(11, 1001, 500, 100.0, 1);
        CartException exception = assertThrows(CartException.class,
                () -> cart.addItem(item11));

        assertTrue(exception.getMessage().contains("cannot contain more than 10 unique items"));
    }

    // ========== BOUNDARY TESTS: MAX TOTAL ITEMS (30) ==========

    @Test
    void shouldAllowExactly30Items() {
        // Given & When - Boundary value (exactly 30 items)
        cart.addItem(new DefaultItem(1, 1001, 500, 100.0, 10));
        cart.addItem(new DefaultItem(2, 1001, 500, 100.0, 10));
        cart.addItem(new DefaultItem(3, 1001, 500, 100.0, 10));

        // Then
        int totalItems = cart.getItems().stream()
                .mapToInt(Item::getQuantity)
                .sum();
        assertEquals(30, totalItems);
    }

    @Test
    void shouldThrowExceptionWhenMaxTotalItemsExceeded() {
        // Given - At boundary (30 items)
        cart.addItem(new DefaultItem(1, 1001, 500, 100.0, 10));
        cart.addItem(new DefaultItem(2, 1001, 500, 100.0, 10));
        cart.addItem(new DefaultItem(3, 1001, 500, 100.0, 10));

        // When & Then - Exceeding boundary (31 items)
        DefaultItem item4 = new DefaultItem(4, 1001, 500, 100.0, 1);
        CartException exception = assertThrows(CartException.class,
                () -> cart.addItem(item4));

        assertTrue(exception.getMessage().contains("cannot exceed 30"));
    }

    @Test
    void shouldAllowJustBelowMaxTotalItems() {
        // Given & When - Below boundary (29 items)
        cart.addItem(new DefaultItem(1, 1001, 500, 100.0, 10));
        cart.addItem(new DefaultItem(2, 1001, 500, 100.0, 10));
        cart.addItem(new DefaultItem(3, 1001, 500, 100.0, 9));

        // Then
        int totalItems = cart.getItems().stream()
                .mapToInt(Item::getQuantity)
                .sum();
        assertEquals(29, totalItems);
    }

    // ========== BOUNDARY TESTS: MAX AMOUNT (500,000 TL) ==========

    @Test
    void shouldAllowExactly500000TL() {
        // Given & When - Boundary value (exactly 500,000 TL)
        cart.addItem(new DefaultItem(1, 1001, 500, 500000.0, 1));

        // Then
        assertEquals(500000.0, cart.calculateTotalPrice());
    }

    @Test
    void shouldThrowExceptionWhenMaxTotalAmountExceeded() {
        // Given & When - Exceeding boundary (500,001 TL)
        DefaultItem expensiveItem = new DefaultItem(1, 1001, 500, 500001.0, 1);

        // Then
        CartException exception = assertThrows(CartException.class,
                () -> cart.addItem(expensiveItem));

        assertTrue(exception.getMessage().contains("cannot exceed 500000.0 TL"));
    }

    @Test
    void shouldAllowJustBelowMaxTotalAmount() {
        // Given & When - Below boundary (499,999 TL)
        cart.addItem(new DefaultItem(1, 1001, 500, 499999.0, 1));

        // Then
        assertEquals(499999.0, cart.calculateTotalPrice());
    }

    // ========== VAS ITEM TESTS ==========

    @Test
    void shouldAddVasItemToDefaultItem() {
        // Given
        DefaultItem item = new DefaultItem(1, 3004, 500, 5000.0, 1);
        cart.addItem(item);
        VasItem vasItem = new VasItem(10, 3242, 5003, 500.0, 1);

        // When
        cart.addVasItemToItem(1, vasItem);

        // Then
        DefaultItem addedItem = (DefaultItem) cart.getItems().getFirst();
        assertEquals(1, addedItem.getVasItems().size());
    }

    @Test
    void shouldThrowExceptionWhenAddingVasItemToNonExistentItem() {
        // Given
        VasItem vasItem = new VasItem(10, 3242, 5003, 500.0, 1);

        // When & Then
        CartException exception = assertThrows(CartException.class,
                () -> cart.addVasItemToItem(999, vasItem));

        assertTrue(exception.getMessage().contains("Item not found"));
    }

    @Test
    void shouldThrowExceptionWhenAddingVasItemToDigitalItem() {
        // Given
        DigitalItem digitalItem = new DigitalItem(1, 7889, 500, 100.0, 1);
        cart.addItem(digitalItem);
        VasItem vasItem = new VasItem(10, 3242, 5003, 50.0, 1);

        // When & Then
        CartException exception = assertThrows(CartException.class,
                () -> cart.addVasItemToItem(1, vasItem));

        assertTrue(exception.getMessage().contains("VasItem can only be added to DefaultItem"));
    }

    // ========== REMOVE & RESET TESTS ==========

    @Test
    void shouldRemoveItemSuccessfully() {
        // Given
        cart.addItem(new DefaultItem(1, 1001, 500, 1000.0, 2));
        cart.addItem(new DefaultItem(2, 1001, 500, 500.0, 1));

        // When
        boolean removed = cart.removeItem(1);

        // Then
        assertTrue(removed);
        assertEquals(1, cart.getItems().size());
        assertEquals(2, cart.getItems().getFirst().getItemId());
    }

    @Test
    void shouldReturnFalseWhenRemovingNonExistentItem() {
        // Given
        cart.addItem(new DefaultItem(1, 1001, 500, 1000.0, 2));

        // When
        boolean removed = cart.removeItem(999);

        // Then
        assertFalse(removed);
        assertEquals(1, cart.getItems().size());
    }

    @Test
    void shouldResetCartSuccessfully() {
        // Given
        cart.addItem(new DefaultItem(1, 1001, 500, 1000.0, 2));
        cart.addItem(new DefaultItem(2, 1001, 500, 500.0, 1));
        cart.setAppliedPromotionId(9909);
        cart.setTotalDiscount(100.0);

        // When
        cart.reset();

        // Then
        assertEquals(0, cart.getItems().size());
        assertEquals(0, cart.getAppliedPromotionId());
        assertEquals(0.0, cart.getTotalDiscount());
        assertEquals(0.0, cart.calculateTotalPrice());
    }

    // ========== CALCULATION TESTS ==========

    @Test
    void shouldCalculateTotalPriceIncludingVasItems() {
        // Given
        DefaultItem item = new DefaultItem(1, 3004, 500, 5000.0, 1);
        item.addVasItem(new VasItem(10, 3242, 5003, 500.0, 1));
        item.addVasItem(new VasItem(11, 3242, 5003, 300.0, 2));
        cart.addItem(item);

        // When
        double totalPrice = cart.calculateTotalPrice();

        // Then
        // Item: 5000 * 1 = 5000
        // VasItem1: 500 * 1 = 500
        // VasItem2: 300 * 2 = 600
        // Total: 6100
        assertEquals(6100.0, totalPrice);
    }

    @Test
    void shouldCalculateTotalAmountWithDiscount() {
        // Given
        cart.addItem(new DefaultItem(1, 1001, 500, 1000.0, 2));
        cart.setTotalDiscount(200.0);

        // When
        double totalAmount = cart.getTotalAmount();

        // Then
        assertEquals(1800.0, totalAmount); // 2000 - 200
    }
}