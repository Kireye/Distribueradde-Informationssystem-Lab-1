package com.werkstrom.distinfolab1.bo;

import com.werkstrom.distinfolab1.bo.enums.OrderStatus;

import java.util.ArrayList;
import java.util.List;

public class OrderInfo {

    private final int orderId;
    private final int ownerId;
    private final List<ItemInfo> items;
    private final OrderStatus status;

    public OrderInfo(int orderId, int ownerId, List<ItemInfo> items, OrderStatus status) {
        this.orderId = orderId;
        this.ownerId = ownerId;
        if (items == null) {
            items = new ArrayList<>();
        }
        this.items = new ArrayList<>(items);
        this.status = status;
    }

    public int getOrderId() {
        return orderId;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public List<ItemInfo> getItems() {
        return new ArrayList<>(items);
    }

    public OrderStatus getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "OrderInfo{" +
                "orderId=" + orderId +
                ", ownerId=" + ownerId +
                ", items=" + items +
                ", status=" + status +
                '}';
    }
}