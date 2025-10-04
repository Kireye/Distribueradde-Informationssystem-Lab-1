package com.werkstrom.distinfolab1.bo;

import com.werkstrom.distinfolab1.bo.enums.UserRole;

import java.util.List;
import java.util.ArrayList;


public class ItemFacade {

    private static final List<Item> items = new ArrayList<>();

    public static List<ItemInfo> getAllItems() {
        List<ItemInfo> result = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            result.add()
        }
    }

    public static List<ItemInfo> getItemsByName(String name, int minPrice, int maxPrice, boolean onlyInStock) {

    }

    public static List<ItemInfo> getItemsByCategory(ItemCategory category, int minPrice, int maxPrice, boolean onlyInStock) {

    }

    public static void addItem(Item item, UserRole role) {

    }

    public static void deleteItem(int id, UserRole role) {

    }

    public static void updateItem(Item item, UserRole role) {

    }
}
