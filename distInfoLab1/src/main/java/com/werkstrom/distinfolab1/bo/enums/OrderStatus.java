package com.werkstrom.distinfolab1.bo.enums;

public enum OrderStatus {
    ORDERED("ordered"),
    PACKAGED("packaged"),
    SHIPPED("shipped"),
    DELIVERED("delivered");

    private final String statusName;

    OrderStatus(String statusName) {
        this.statusName = statusName;
    }
    public String getStatusName() {
        return statusName;
    }

}