package com.werkstrom.distinfolab1.bo.facades;

import com.werkstrom.distinfolab1.db.MySqlConnectionManager;
import com.werkstrom.distinfolab1.db.MySqlItem;
import com.werkstrom.distinfolab1.db.exceptions.ConnectionException;
import com.werkstrom.distinfolab1.db.exceptions.NoResultException;
import com.werkstrom.distinfolab1.db.exceptions.QueryException;
import com.werkstrom.distinfolab1.ui.ItemInfo;
import com.werkstrom.distinfolab1.bo.Item;
import com.werkstrom.distinfolab1.bo.ItemCategory;

import java.util.ArrayList;
import java.util.List;

public final class ItemFacade {

    private ItemFacade() {}

    public static List<ItemInfo> listAll(boolean inStockOnly) throws ConnectionException, QueryException, NoResultException {
        ensureConnected();
        List<MySqlItem> dbItems = MySqlItem.getAllItems(inStockOnly);
        return toInfo(dbItems);
    }

    public static List<ItemInfo> listByCategory(int categoryId, boolean inStockOnly) throws ConnectionException, QueryException, NoResultException {
        if (categoryId <= 0) {
            throw new IllegalArgumentException("categoryId must be > 0");
        }
        ensureConnected();
        List<MySqlItem> dbItems = MySqlItem.getItemsByCategory(categoryId, inStockOnly);
        return toInfo(dbItems);
    }

    public static List<ItemInfo> searchByName(String searchTerm, boolean inStockOnly) throws ConnectionException, QueryException, NoResultException {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            throw new IllegalArgumentException("searchTerm cannot be empty");
        }
        ensureConnected();

        // NOTE: tills MySqlItem.getItemsByName() är fixad i DB-lagret filtrerar vi i minnet:
        List<MySqlItem> all = MySqlItem.getAllItems(inStockOnly);
        String q = searchTerm.trim().toLowerCase();
        List<MySqlItem> filtered = new ArrayList<>();
        for (MySqlItem it : all) {
            if (it.getName().toLowerCase().contains(q) ||
                    it.getDescription().toLowerCase().contains(q)) {
                filtered.add(it);
            }
        }
        if (filtered.isEmpty()) {
            throw new NoResultException("No items found");
        }
        return toInfo(filtered);
    }

    private static void ensureConnected() throws ConnectionException {
        if (!MySqlConnectionManager.isConnected()) {
            throw new ConnectionException("No database connection.");
        }
    }

    private static List<ItemInfo> toInfo(List<MySqlItem> items) {
        List<ItemInfo> out = new ArrayList<>();
        for (Item it : items) {
            out.add(new ItemInfo(
                    it.getId(),
                    it.getName(),
                    it.getDescription(),
                    it.getPrice(),
                    it.getStock(),
                    it.getCategories() == null ? new ArrayList<ItemCategory>() : it.getCategories()
            ));
        }
        return out;
    }
}