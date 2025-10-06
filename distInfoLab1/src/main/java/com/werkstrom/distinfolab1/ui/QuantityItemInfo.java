package com.werkstrom.distinfolab1.ui;

public class QuantityItemInfo {
    private final ItemInfo item;
    private final int quantity;

    public QuantityItemInfo(ItemInfo item, int quantity) {
        if (item == null) throw new IllegalArgumentException("item cannot be null");
        if (quantity <= 0) throw new IllegalArgumentException("quantity must be > 0");
        this.item = item;
        this.quantity = quantity;
    }

    public ItemInfo getItem() { return item; }

    public int getQuantity() { return quantity; }

    public float getSubtotal() { return item.getPrice() * quantity; }
}
