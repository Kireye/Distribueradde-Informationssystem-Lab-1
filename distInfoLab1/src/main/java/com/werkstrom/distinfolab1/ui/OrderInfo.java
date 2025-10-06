package com.werkstrom.distinfolab1.ui;

import com.werkstrom.distinfolab1.bo.enums.OrderStatus;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderInfo {
    private final int orderId;
    private final int ownerId;
    private final Date orderDate;
    private final List<OrderLineInfo> lines;
    private final OrderStatus status;

    public OrderInfo(int orderId, int ownerId, Date orderDate, List<OrderLineInfo> lines, OrderStatus status) {
        this.orderId = orderId;
        this.ownerId = ownerId;
        this.orderDate = orderDate;
        this.lines = lines == null ? new ArrayList<>() : new ArrayList<>(lines);
        this.status = status;
    }

    public int getOrderId() { return orderId; }
    public int getOwnerId() { return ownerId; }
    public Date getOrderDate() { return orderDate; }
    public List<OrderLineInfo> getLines() { return new ArrayList<>(lines); }
    public OrderStatus getStatus() { return status; }

    public float getTotal() {
        float sum = 0f;
        for (OrderLineInfo l : lines) sum += l.getSubtotal();
        return sum;
    }

    @Override
    public String toString() {
        return "OrderInfo{" +
                "orderId=" + orderId +
                ", ownerId=" + ownerId +
                ", orderDate=" + orderDate +
                ", lines=" + lines +
                ", status=" + status +
                '}';
    }
}
