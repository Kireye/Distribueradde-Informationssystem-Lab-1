package com.werkstrom.distinfolab1.ui;

public class OrderLineInfo {
    private final ItemInfo item;
    private final int quantity;

    public OrderLineInfo(ItemInfo item, int quantity) {
        if (item == null) throw new IllegalArgumentException("item cannot be null");
        if (quantity <= 0) throw new IllegalArgumentException("quantity must be > 0");
        this.item = item;
        this.quantity = quantity;
    }

    public ItemInfo getItem() { return item; }
    public int getQuantity() { return quantity; }

    public float getSubtotal() {
        return item.getPrice() * quantity;
    }

    @Override
    public String toString() {
        return "OrderLineInfo{item=" + item + ", quantity=" + quantity + "}";
    }
}