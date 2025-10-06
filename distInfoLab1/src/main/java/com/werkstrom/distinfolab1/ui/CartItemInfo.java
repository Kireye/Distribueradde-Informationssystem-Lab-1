package com.werkstrom.distinfolab1.ui;

import com.werkstrom.distinfolab1.bo.ItemCategory;

import java.util.ArrayList;
import java.util.List;

public class CartItemInfo {
    private final int itemId;
    private final String name;
    private final float price;
    private final int stock;
    private final int quantity;
    private final List<ItemCategory> categories;

    public CartItemInfo(int itemId, String name, float price, int stock, int quantity, List<ItemCategory> categories) {
        this.itemId = itemId;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.quantity = quantity;
        this.categories = categories == null ? new ArrayList<>() : new ArrayList<>(categories);
    }

    public int getItemId() { return itemId; }
    public String getName() { return name; }
    public float getPrice() { return price; }
    public int getStock() { return stock; }
    public int getQuantity() { return quantity; }
    public List<ItemCategory> getCategories() { return new ArrayList<>(categories); }

    public float getSubtotal() { return price * quantity; }

    @Override
    public String toString() {
        return "CartItemInfo{" +
                "itemId=" + itemId +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", stock=" + stock +
                ", quantity=" + quantity +
                ", categories=" + categories +
                '}';
    }
}
