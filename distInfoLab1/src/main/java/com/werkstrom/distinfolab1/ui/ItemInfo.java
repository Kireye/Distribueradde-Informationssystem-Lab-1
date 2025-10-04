package com.werkstrom.distinfolab1.ui;

import com.werkstrom.distinfolab1.bo.ItemCategory;

import java.util.ArrayList;
import java.util.List;

public class ItemInfo {

    private final int id;
    private final String name;
    private final String description;
    private final float price;
    private final int stock;
    private final List<ItemCategory> categories;

    public ItemInfo(int id, String name, String description, float price, int stock, List<ItemCategory> categories) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        if (categories == null) {
            categories = new ArrayList<>();
        }
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

    @Override
    public String toString() {
        return "ItemInfo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", stock=" + stock +
                ", categories=" + categories +
                '}';
    }
}
