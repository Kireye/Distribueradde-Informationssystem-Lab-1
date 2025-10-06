package com.werkstrom.distinfolab1.bo.facades;

import com.werkstrom.distinfolab1.bo.ItemCategory;
import com.werkstrom.distinfolab1.db.MySqlConnectionManager;
import com.werkstrom.distinfolab1.db.MySqlItemCategory;
import com.werkstrom.distinfolab1.db.exceptions.ConnectionException;
import com.werkstrom.distinfolab1.db.exceptions.NoResultException;
import com.werkstrom.distinfolab1.db.exceptions.QueryException;
import com.werkstrom.distinfolab1.ui.ItemCategoryInfo;

import java.util.ArrayList;
import java.util.List;

public final class ItemCategoryFacade {

    private ItemCategoryFacade() {}

    public static List<ItemCategoryInfo> getAllCategories()
            throws ConnectionException, QueryException, NoResultException {
        ensureConnected();
        List<MySqlItemCategory> dbCats = MySqlItemCategory.getAllItemCategories();
        List<ItemCategoryInfo> out = new ArrayList<>();
        for (ItemCategory c : dbCats) {
            if (c != null) out.add(new ItemCategoryInfo(c.getId(), c.getName()));
        }
        return out;
    }

    private static void ensureConnected() throws ConnectionException {
        if (!MySqlConnectionManager.isConnected()) {
            throw new ConnectionException("No database connection.");
        }
    }
}
