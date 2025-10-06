package com.werkstrom.distinfolab1.bo.facades;

import com.werkstrom.distinfolab1.bo.Item;
import com.werkstrom.distinfolab1.bo.ItemCategory;
import com.werkstrom.distinfolab1.db.MySqlConnectionManager;
import com.werkstrom.distinfolab1.db.MySqlItem;
import com.werkstrom.distinfolab1.db.exceptions.ConnectionException;
import com.werkstrom.distinfolab1.db.exceptions.QueryException;
import com.werkstrom.distinfolab1.ui.ItemInfo;

import java.util.ArrayList;
import java.util.List;

public final class ItemFacade {

    private ItemFacade() { }

    public static List<ItemInfo> getAllItems(boolean onlyInStock) throws ConnectionException, QueryException {
        ensureConnected();
        List<MySqlItem> dbItems = MySqlItem.getAllItems(onlyInStock);
        return toItemInfoList(dbItems);
    }

    public static List<ItemInfo> search(String q, Integer categoryId, boolean onlyInStock) throws ConnectionException, QueryException {
        ensureConnected();

        if (q != null && !q.isEmpty()) {
            return toItemInfoList(MySqlItem.getItemsByName(q, onlyInStock));
        }
        if (categoryId != null && categoryId > 0) {
            return toItemInfoList(MySqlItem.getItemsByCategory(categoryId, onlyInStock));
        }
        return toItemInfoList(MySqlItem.getAllItems(onlyInStock));
    }

    public static List<ItemInfo> getItemsByName(String searchTerm, boolean onlyInStock) throws ConnectionException, QueryException {
        if (searchTerm == null) throw new IllegalArgumentException("searchTerm cannot be null");
        if (searchTerm.isEmpty()) throw new IllegalArgumentException("searchTerm cannot be empty");
        ensureConnected();
        List<MySqlItem> dbItems = MySqlItem.getItemsByName(searchTerm, onlyInStock);
        return toItemInfoList(dbItems);
    }

    public static List<ItemInfo> getItemsByCategory(int categoryId, boolean onlyInStock) throws ConnectionException, QueryException {
        if (categoryId <= 0) throw new IllegalArgumentException("categoryId must be greater than 0");
        ensureConnected();
        List<MySqlItem> dbItems = MySqlItem.getItemsByCategory(categoryId, onlyInStock);
        return toItemInfoList(dbItems);
    }

    private static void ensureConnected() throws ConnectionException {
        if (!MySqlConnectionManager.isConnected()) {
            throw new ConnectionException("No database connection. Initialize connection before calling ItemFacade methods.");
        }
    }

    private static List<ItemInfo> toItemInfoList(List<? extends Item> items) {
        List<ItemInfo> result = new ArrayList<>();
        if (items == null) return result;
        for (Item it : items) {
            if (it != null) result.add(toItemInfo(it));
        }
        return result;
    }

    private static ItemInfo toItemInfo(Item it) {
        int id = it.getId();
        String name = it.getName();
        String description = it.getDescription();
        float price = it.getPrice();
        int stock = it.getStock();

        List<ItemCategory> categories = it.getCategories();
        List<ItemCategory> categoriesCopy = new ArrayList<>();
        if (categories != null) {
            for (ItemCategory c : categories) {
                if (c != null) categoriesCopy.add(c);
            }
        }
        return new ItemInfo(id, name, description, price, stock, categoriesCopy);
    }
}