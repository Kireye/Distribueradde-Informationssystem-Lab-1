package com.werkstrom.distinfolab1.bo.facades;

import com.werkstrom.distinfolab1.bo.Item;
import com.werkstrom.distinfolab1.bo.ItemCategory;
import com.werkstrom.distinfolab1.bo.enums.UserRole;
import com.werkstrom.distinfolab1.ui.ItemInfo;

import java.util.List;
import java.util.ArrayList;


public class ItemFacade {

    private static final List<Item> items = new ArrayList<>();

    public static List<ItemInfo> getAllItems() {
        List<ItemInfo> result = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            Item current = items.get(i);
            ItemInfo info = toInfo(current);
            result.add(info);
        }
        return result;
    }

    public static List<ItemInfo> getItemsByName(String name, int minPrice, int maxPrice, boolean onlyInStock) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("name cannot be null or empty");
        }
        if (minPrice < 0) {
            minPrice = 0;
        }
        if (maxPrice < minPrice) {
            maxPrice = minPrice;
        }

        String needle = name.toLowerCase();
        List<ItemInfo> result = new ArrayList<>();

        for (int i = 0; i < items.size(); i++) {
            Item it = items.get(i);

            String itemName = it.getName();
            boolean nameMatches = false;
            if (itemName != null) {
                String lower = itemName.toLowerCase();
                if (lower.contains(needle)) {
                    nameMatches = true;
                }
            }

            float price = it.getPrice();
            boolean priceMatches = false;
            if (price >= minPrice && price <= maxPrice) {
                priceMatches = true;
            }

            boolean stockMatches = true;
            if (onlyInStock) {
                if (it.getStock() <= 0) {
                    stockMatches = false;
                }
            }

            if (nameMatches && priceMatches && stockMatches) {
                result.add(toInfo(it));
            }
        }

        return result;
    }

    public static List<ItemInfo> getItemsByCategory(ItemCategory category, int minPrice, int maxPrice, boolean onlyInStock) {
        if (category == null) {
            throw new IllegalArgumentException("category cannot be null");
        }
        if (minPrice < 0) {
            minPrice = 0;
        }
        if (maxPrice < minPrice) {
            maxPrice = minPrice;
        }

        int wantedCategoryId = category.getId();
        List<ItemInfo> result = new ArrayList<>();

        for (int i = 0; i < items.size(); i++) {
            Item it = items.get(i);
        }

        return result;
    }

    public static void addItem(Item item, UserRole role) {

    }

    public static void deleteItem(int id, UserRole role) {

    }

    public static void updateItem(Item item, UserRole role) {

    }

    private static ItemInfo toInfo(Item it) {
        return new ItemInfo(
                it.getId(),
                it.getName(),
                it.getDescription(),
                it.getPrice(),
                it.getStock(),
                it.getCategories()
        );
    }
}