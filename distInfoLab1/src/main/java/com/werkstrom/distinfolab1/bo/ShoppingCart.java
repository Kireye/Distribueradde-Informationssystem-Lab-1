package com.werkstrom.distinfolab1.bo;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCart {

    private final int ownerId;
    private final List<QuantityItem> cartItems;

    public ShoppingCart(int ownerId, List<QuantityItem> items) {
        if (ownerId <= 0) {
            throw new IllegalArgumentException("owner id must be greater than 0");
        }
        if (items == null) {
            items = new ArrayList<>();
        }
        this.ownerId = ownerId;
        this.cartItems = new ArrayList<>(items);
    }

    public int getOwnerId() {
        return ownerId;
    }

    public List<QuantityItem> getCartItems() {
        return new ArrayList<>(cartItems);
    }

    public float getTotalPrice() {
        float totalPrice = 0;
        for (QuantityItem cartItem : cartItems) {
            totalPrice += cartItem.getItem().getPrice();
        }
        return totalPrice;
    }

    public void addItem(Item item, int quantity) {
        if (item == null) throw new IllegalArgumentException("item cannot be null");
        if (quantity < 0) throw new IllegalArgumentException("quantity cannot be negative");

        int itemIndex = getItemIndex(item);
        if (itemIndex > -1) cartItems.get(itemIndex).addQuantity(quantity);
        else cartItems.add(new QuantityItem(quantity, item));
    }

    public void addItem(Item item) {
        addItem(item, 1);
    }

    public void removeItem(Item item, int quantity) {
        if (item == null) throw new IllegalArgumentException("item cannot be null");
        if (quantity < 0) throw new IllegalArgumentException("quantity cannot be negative");

        int itemIndex = getItemIndex(item);
        if (itemIndex > -1) cartItems.get(itemIndex).removeQuantity(quantity);
        if (cartItems.get(itemIndex).getQuantity() <= 0) cartItems.remove(itemIndex);
    }

    public void removeItem(Item item) {
        if (item == null) throw new IllegalArgumentException("item cannot be null");
        removeItem(item, cartItems.get(getItemIndex(item)).getQuantity());
    }

    public int getItemQuantity(Item item) {
        if (item == null) return 0;
        int itemIndex = getItemIndex(item);
        if (itemIndex > -1) return cartItems.get(itemIndex).getQuantity();
        return 0;
    }

    public void clearItems() {
        cartItems.clear();
    }

    private int getItemIndex(Item item) {
        if (item == null)
            return -1;

        for  (int i = 0; i < cartItems.size(); i++) {
            if (cartItems.get(i).getItem().equals(item)) return i;
        }
        return -1;
    }

    @Override
    public String toString() {
        return "ShoppingCart{" +
                "ownerId=" + ownerId +
                ", items=" + cartItems +
                '}';
    }
}