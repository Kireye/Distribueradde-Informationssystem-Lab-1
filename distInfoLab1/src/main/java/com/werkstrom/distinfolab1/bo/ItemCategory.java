package com.werkstrom.distinfolab1.bo;

public class ItemCategory {

    private final int id;
    private final String name;

    public ItemCategory(int id, String name) {
        if (id <= 0) {
            throw new IllegalArgumentException("id must be greater than 0");
        }
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("name cannot be null or empty");
        }
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "ItemCategory{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}