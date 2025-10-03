package com.werkstrom.distinfolab1.bo;

import java.util.ArrayList;
import java.util.List;

public class Order {

    private final int orderId;
    private final int ownerId;
    private List<Item> items;
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
        if (status == null) {
            throw new IllegalArgumentException("order status cannot be null");
        }
        this.orderId = orderId;
        this.ownerId = ownerId;
        this.items = new ArrayList<>(items);
        this.status = status;
    }

    public int getOrderId() {
        return orderId;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public List<Item> getItems() {
        return new ArrayList<>(items);
    }

    public OrderStatus getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", ownerId=" + ownerId +
                ", items=" + items +
                ", status=" + status +
                '}';
    }
}
