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


}
