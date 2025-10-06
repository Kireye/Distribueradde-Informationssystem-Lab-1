package com.werkstrom.distinfolab1.ui;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCartInfo {
    private final int ownerId;
    private final List<CartItemInfo> items;

    public ShoppingCartInfo(int ownerId, List<CartItemInfo> items) {
        this.ownerId = ownerId;
        this.items = items == null ? new ArrayList<>() : new ArrayList<>(items);
    }

    public int getOwnerId() { return ownerId; }
    public List<CartItemInfo> getItems() { return new ArrayList<>(items); }

    public float getTotal() {
        float sum = 0f;
        for (CartItemInfo ci : items) sum += ci.getSubtotal();
        return sum;
    }

    public int getTotalQuantity() {
        int n = 0;
        for (CartItemInfo ci : items) n += ci.getQuantity();
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
