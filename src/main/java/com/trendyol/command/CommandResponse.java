package com.trendyol.command;

import com.trendyol.domain.cart.Cart;
import com.trendyol.domain.item.DefaultItem;
import com.trendyol.domain.item.Item;
import com.trendyol.domain.item.VasItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandResponse {

    private boolean result;
    private Object message;

    public CommandResponse() {
    }

    public CommandResponse(boolean result, Object message) {
        this.result = result;
        this.message = message;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }

    public static CommandResponse success(String message) {
        return new CommandResponse(true, message);
    }

    public static CommandResponse error(String message) {
        return new CommandResponse(false, message);
    }

    public static CommandResponse displayCart(Cart cart) {
        Map<String, Object> cartData = new HashMap<>();

        List<Map<String, Object>> itemsList = convertItemsToMap(cart.getItems());

        cartData.put("items", itemsList);
        cartData.put("totalAmount", cart.getTotalAmount());
        cartData.put("appliedPromotionId", cart.getAppliedPromotionId());
        cartData.put("totalDiscount", cart.getTotalDiscount());

        return new CommandResponse(true, cartData);
    }

    private static List<Map<String, Object>> convertItemsToMap(List<Item> items) {
        List<Map<String, Object>> itemsList = new ArrayList<>();

        for (Item item : items) {
            Map<String, Object> itemData = convertItemToMap(item);
            itemsList.add(itemData);
        }

        return itemsList;
    }

    private static Map<String, Object> convertItemToMap(Item item) {
        Map<String, Object> itemData = new HashMap<>();
        itemData.put("itemId", item.getItemId());
        itemData.put("categoryId", item.getCategoryId());
        itemData.put("sellerId", item.getSellerId());
        itemData.put("price", item.getPrice());
        itemData.put("quantity", item.getQuantity());

        // VasItems add
        if (item instanceof DefaultItem defaultItem) {
            itemData.put("vasItems", convertVasItemsToMap(defaultItem.getVasItems()));
        } else {
            itemData.put("vasItems", new ArrayList<>());
        }

        return itemData;
    }

    private static List<Map<String, Object>> convertVasItemsToMap(List<VasItem> vasItems) {
        List<Map<String, Object>> vasItemsList = new ArrayList<>();

        for (VasItem vasItem : vasItems) {
            Map<String, Object> vasItemData = new HashMap<>();
            vasItemData.put("vasItemId", vasItem.getVasItemId());
            vasItemData.put("vasCategoryId", vasItem.getCategoryId());
            vasItemData.put("vasSellerId", vasItem.getSellerId());
            vasItemData.put("price", vasItem.getPrice());
            vasItemData.put("quantity", vasItem.getQuantity());
            vasItemsList.add(vasItemData);
        }

        return vasItemsList;
    }
}