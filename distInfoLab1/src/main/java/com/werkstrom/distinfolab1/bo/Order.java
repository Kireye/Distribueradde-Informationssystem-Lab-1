package com.werkstrom.distinfolab1.bo;

import com.werkstrom.distinfolab1.bo.enums.OrderStatus;

import java.util.ArrayList;
import java.util.List;

public class Order {

    private final int orderId;
    private final int ownerId;
    private final List<QuantityItem> orderItems;
    private OrderStatus status;

    public Order(int orderId, int ownerId, List<QuantityItem> items, OrderStatus status) {
        if (orderId <= 0) {
            throw new IllegalArgumentException("order id must be greater than 0");
        }
        if (ownerId <= 0) {
            throw new IllegalArgumentException("owner id must be greater than 0");
        }
        if (items == null) {
            items = new ArrayList<>();
        }
        for (QuantityItem it : items) {
            if (it == null) {
                throw new IllegalArgumentException("items cannot contain null");
            }
        }
        if (status == null) {
            throw new IllegalArgumentException("order status cannot be null");
        }
        this.orderId = orderId;
        this.ownerId = ownerId;
        this.orderItems = new ArrayList<>(items);
        this.status = status;
    }

    public int getOrderId() {
        return orderId;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public List<QuantityItem> getOrderItems() {
        return new ArrayList<>(orderItems);
    }

    public void addItem(Item item, int quantity) {
        if (item == null) throw new IllegalArgumentException("item cannot be null");
        if (quantity < 0) throw new IllegalArgumentException("quantity cannot be negative");

        int itemIndex = getItemIndex(item);
        if (itemIndex > -1) orderItems.get(itemIndex).addQuantity(quantity);
        else orderItems.add(new QuantityItem(quantity, item));
    }

    public void addItem(Item item) {
        addItem(item, 1);
    }

    public void removeItem(Item item, int quantity) {
        if (item == null) throw new IllegalArgumentException("item cannot be null");
        if (quantity < 0) throw new IllegalArgumentException("quantity cannot be negative");

        int itemIndex = getItemIndex(item);
        if (itemIndex > -1) orderItems.get(itemIndex).removeQuantity(quantity);
        if (orderItems.get(itemIndex).getQuantity() <= 0) orderItems.remove(itemIndex);
    }

    public void removeItem(Item item) {
        if (item == null) throw new IllegalArgumentException("item cannot be null");
        removeItem(item, orderItems.get(getItemIndex(item)).getQuantity());
    }

    public int getItemQuantity(Item item) {
        if (item == null) return 0;
        int itemIndex = getItemIndex(item);
        if (itemIndex > -1) return orderItems.get(itemIndex).getQuantity();
        return 0;
    }

    public int getNrOfItems() {
        return orderItems.size();
    }

    public float getTotalPrice() {
        float totalPrice = 0;
        for (QuantityItem cartItem : orderItems) {
            totalPrice += cartItem.getItem().getPrice();
        }
        return totalPrice;
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

    private int getItemIndex(Item item) {
        if (item == null)
            return -1;

        for  (int i = 0; i < orderItems.size(); i++) {
            if (orderItems.get(i).getItem().equals(item)) return i;
        }
        return -1;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", ownerId=" + ownerId +
                ", orderItems=" + orderItems +
                ", nrOfItems=" + orderItems.size() +
                ", status=" + status +
                '}';
    }
}