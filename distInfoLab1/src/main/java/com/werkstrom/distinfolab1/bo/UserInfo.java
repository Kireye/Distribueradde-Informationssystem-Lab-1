package com.werkstrom.distinfolab1.bo;

import com.werkstrom.distinfolab1.bo.enums.UserRole;

import java.util.ArrayList;
import java.util.List;

public class UserInfo {

    private final int id;
    private final UserRole role;
    private final String name;
    private final String email;
    private final ShoppingCart cart;
    private final List<Order> orders;

    public UserInfo(int id, UserRole role, String name, String email, ShoppingCart cart, List<Order> orders) {
        this.id = id;
        this.role = role;
        this.name = name;
        this.email = email;
        this.cart = cart;
        if (orders == null) {
            orders = new ArrayList<>();
        }
        this.orders = new ArrayList<>(orders);
    }

    public int getId() {
        return id;
    }

    public UserRole getRole() {
        return role;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public ShoppingCart getCart() {
        return cart;
    }

    public List<Order> getOrders() {
        return new ArrayList<>(orders);
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "id=" + id +
                ", role=" + role +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", cart=" + cart +
                ", orders=" + orders +
                '}';
    }
}