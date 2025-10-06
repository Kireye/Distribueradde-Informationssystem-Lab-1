package com.werkstrom.distinfolab1.ui;

public class ItemCategoryInfo {
    private final int id;
    private final String name;

    public ItemCategoryInfo(int id, String name) {
        if (id <= 0) throw new IllegalArgumentException("id must be > 0");
        if (name == null || name.isEmpty()) throw new IllegalArgumentException("name cannot be null or empty");
        this.id = id;
        this.name = name;
    }

    public int getId() { return id; }

    public String getName() { return name; }

    @Override
    public String toString() {
        return "ItemCategoryInfo{" + "id=" + id + ", name='" + name + '\'' + '}';
    }
}