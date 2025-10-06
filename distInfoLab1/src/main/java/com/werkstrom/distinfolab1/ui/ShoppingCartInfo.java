package com.werkstrom.distinfolab1.ui;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCartInfo {
    private final int ownerId;
    private final List<QuantityItemInfo> items;

    public ShoppingCartInfo(int ownerId, List<QuantityItemInfo> items) {
        this.ownerId = ownerId;
        this.items = items == null ? new ArrayList<>() : new ArrayList<>(items);
    }

    public int getOwnerId() { return ownerId; }

    public List<QuantityItemInfo> getItems() { return new ArrayList<>(items); }

    public float getTotal() {
        float sum = 0f;
        for (QuantityItemInfo qi : items) sum += qi.getSubtotal();
        return sum;
    }

    public int getTotalQuantity() {
        int n = 0;
        for (QuantityItemInfo qi : items) n += qi.getQuantity();
        return n;
    }

    @Override
    public String toString() {
        return "ShoppingCartInfo{" +
                "ownerId=" + ownerId +
                ", items=" + items +
                '}';
    }
}
