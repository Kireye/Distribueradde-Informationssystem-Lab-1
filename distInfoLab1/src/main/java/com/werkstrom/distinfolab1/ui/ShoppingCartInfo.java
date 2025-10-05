package com.werkstrom.distinfolab1.ui;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCartInfo {

    private final int ownerId;
    private final List<CartItemInfo> items;

    public ShoppingCartInfo(int ownerId, List<CartItemInfo> items) {
        this.ownerId = ownerId;
        if (items == null) {
            items = new ArrayList<>();
        }
        this.items = new ArrayList<>(items);
    }

    public int getOwnerId() {
        return ownerId;
    }

    public List<CartItemInfo> getItems() {
        return new ArrayList<>(items);
    }

    public float getTotalPrice() {
        float total = 0f;
        for (CartItemInfo line : items) {
            if (line != null && line.getItem() != null) {
                total += line.getItem().getPrice() * line.getQuantity();
            }
        }
        return total;
    }

    public int getTotalQuantity() {
        int sum = 0;
        for (CartItemInfo line : items) {
            if (line != null) sum += line.getQuantity();
        }
        return sum;
    }

    @Override
    public String toString() {
        return "ShoppingCartInfo{" +
                "ownerId=" + ownerId +
                ", items=" + items +
                '}';
    }
}