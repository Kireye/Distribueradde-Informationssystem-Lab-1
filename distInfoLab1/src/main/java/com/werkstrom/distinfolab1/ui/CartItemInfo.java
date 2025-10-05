package com.werkstrom.distinfolab1.ui;

public class CartItemInfo {

    private final int quantity;
    private final ItemInfo item;

    public CartItemInfo(int quantity, ItemInfo item) {
        this.quantity = quantity;
        this.item = item;
    }

    public int getQuantity() {
        return quantity;
    }

    public ItemInfo getItem() {
        return item;
    }

    @Override
    public String toString() {
        return "CartItemInfo{" +
                "quantity=" + quantity +
                ", item=" + item +
                '}';
    }
}