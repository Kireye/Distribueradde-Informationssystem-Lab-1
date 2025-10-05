package com.werkstrom.distinfolab1.bo;

import com.werkstrom.distinfolab1.bo.enums.OrderStatus;

import java.util.ArrayList;
import java.util.List;

public class Order {

    private final int orderId;
    private final int ownerId;
    private final List<Item> cartItems;
    private OrderStatus status;

    public Order(int orderId, int ownerId, List<Item> items, OrderStatus status) {
        if (orderId <= 0) {
            throw new IllegalArgumentException("order id must be greater than 0");
        }
        if (ownerId <= 0) {
            throw new IllegalArgumentException("owner id must be greater than 0");
        }
        if (items == null) {
            items = new ArrayList<>();
        }
        for (Item it : items) {
            if (it == null) {
                throw new IllegalArgumentException("items cannot contain null");
            }
        }
        if (status == null) {
            throw new IllegalArgumentException("order status cannot be null");
        }
        this.orderId = orderId;
        this.ownerId = ownerId;
        this.cartItems = new ArrayList<>(items);
        this.status = status;
    }

    public int getOrderId() {
        return orderId;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public List<Item> getCartItems() {
        return new ArrayList<>(cartItems);
    }

    public void addItem(Item item) {
        cartItems.add(item);
    }

    public int getNrOfItems() {
        return cartItems.size();
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus newStatus) {
        if (newStatus == null) {
            throw new IllegalArgumentException("new status cannot be null");
        }
        status = newStatus;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", ownerId=" + ownerId +
                ", nrOfItems=" + cartItems.size() +
                ", status=" + status +
                '}';
    }
}