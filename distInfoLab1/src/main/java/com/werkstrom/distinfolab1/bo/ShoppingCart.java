package com.werkstrom.distinfolab1.bo;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCart {

    private final int ownerId;
    private final List<Item> items;

    public ShoppingCart(int ownerId, List<Item> items) {
        if (ownerId <= 0) {
            throw new IllegalArgumentException("owner id must be greater than 0");
        }
        if (items == null) {
            items = new ArrayList<>();
        }
        this.ownerId = ownerId;
        this.items = new ArrayList<>(items);
    }

    public int getOwnerId() {
        return ownerId;
    }

    public List<Item> getItems() {
        return new ArrayList<>(items);
    }

    public float getTotalPrice(List<Item> items) {
        float totalPrice = 0;
        for (int i = 0; i < items.size(); i++) {
            totalPrice += items[i].getPrice;
        }
    }

    public void clearItems() {
        for (int i = 0; i < items.size(); i++) {
            items.remove(i);
        }
    }

    @Override
    public String toString() {
        return "ShoppingCart{" +
                "ownerId=" + ownerId +
                ", items=" + items +
                '}';
    }
}