package com.werkstrom.distinfolab1.bo;

public class QuantityItem {
    private int quantity;
    private final Item item;

    public QuantityItem(int quantity, Item item) {
        if (quantity <= 0) throw new IllegalArgumentException("quantity cannot be negative or zero (0)");
        if (item == null) throw new IllegalArgumentException("item cannot be null");
        this.quantity = quantity;
        this.item = item;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        if (quantity < 0) throw new IllegalArgumentException("quantity cannot be negative");
        this.quantity = quantity;
    }

    public void addQuantity(int quantity) {
        if (quantity < 0) throw new IllegalArgumentException("quantity cannot be negative");
        this.quantity += quantity;
    }

    public void removeQuantity(int quantity) {
        if (quantity < 0) throw new IllegalArgumentException("quantity cannot be negative");
        this.quantity -= quantity;
        if (this.quantity < 0) this.quantity = 0;
    }

    public Item getItem() {
        return item;
    }

    @Override
    public String toString() {
        return "QuantityItem{" + "quantity=" + quantity + ", item=" + item + '}';
    }

}
