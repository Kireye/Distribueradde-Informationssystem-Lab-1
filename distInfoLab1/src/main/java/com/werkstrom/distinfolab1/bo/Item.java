package com.werkstrom.distinfolab1.bo;

import java.util.ArrayList;
import java.util.List;

public class Item {

    private final int id;
    private final String name;
    private String description;
    private float price;
    private int stock;
    private final List<ItemCategory> categories;

    public Item(int id, String name, String description, float price, int stock, List<ItemCategory> categories) {
        if (id <= 0) {
            throw new IllegalArgumentException("id must be greater than 0");
        }
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("name cannot be null or empty");
        }
        if (description == null) { // Do we need this really?
            description = "";
        }
        if (price < 0) {
            throw new IllegalArgumentException("price cannot be negative");
        }
        if (stock < 0) {
            throw new IllegalArgumentException("stock must be >= 0");
        }
        if (categories == null) {
            categories = new ArrayList<>();
        }
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.categories = new ArrayList<>(categories);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public float getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }

    public List<ItemCategory> getCategories() {
        return new ArrayList<>(categories);
    }

    public void setDescription(String description) {
        if (description == null) {
            description = "";
        }
        this.description = description;
    }

    public void setPrice(float price) {
        if (price < 0) {
            throw new IllegalArgumentException("price cannot be negative");
        }
        this.price = price;
    }

    public void addStock(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("quantity must be greater than 0");
        }
        stock += quantity;
    }

    public void removeStock(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("quantity must be greater than 0");
        }
        if (stock - quantity < 0) {
            throw new IllegalArgumentException("not enough stock");
        }
        stock -= quantity;
    }

    public void addCategory(ItemCategory category) {
        if (category == null) {
            throw new IllegalArgumentException("category cannot be null");
        }
        if (!categories.contains(category)) {
            categories.add(category);
        }
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", stock=" + stock +
                ", categories=" + categories +
                '}';
    }
}