package com.werkstrom.distinfolab1.bo;

import java.util.ArrayList;
import java.util.List;

public class User {

    private final int id;
    private UserRole role;
    private String name;
    private String email;
    private ShoppingCart cart;
    private final List<Order> orders;

    public User(int id, UserRole role, String name, String email, ShoppingCart cart, List<Order> orders) {
        if (id <= 0) {
            throw new IllegalArgumentException("id must be greater than 0");
        }
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("name cannot be null or empty");
        }
        if (!isEmailValid(email)) {
            throw new IllegalArgumentException("email must contain '@' and cannot be null");
        }
        if (orders == null) {
            orders = new ArrayList<>();
        }
        this.id = id;
        this.role = role;
        this.name = name;
        this.email = email;
        this.cart = cart;
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

    public void addOrder(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("order cannot be null");
        }
        orders.add(order);
    }

    public void setName(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("name cannot be null or empty");
        }
        this.name = name;
    }

    public void setEmail(String email) {
        if (!isEmailValid(email)){
            throw new IllegalArgumentException("email is not valid");
        }
        this.email = email;
    }

    public boolean isEmailValid(String email) { // TODO: check so it only contains ONE @
        return email != null && email.contains("@");
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", role=" + role +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", cart=" + cart +
                ", orders=" + orders +
                '}';
    }
}
