package com.werkstrom.distinfolab1.bo.facades;

import com.werkstrom.distinfolab1.bo.Item;
import com.werkstrom.distinfolab1.bo.ItemCategory;
import com.werkstrom.distinfolab1.bo.QuantityItem;
import com.werkstrom.distinfolab1.bo.ShoppingCart;
import com.werkstrom.distinfolab1.db.MySqlConnectionManager;
import com.werkstrom.distinfolab1.db.MySqlShoppingCart;
import com.werkstrom.distinfolab1.db.exceptions.ConnectionException;
import com.werkstrom.distinfolab1.db.exceptions.QueryException;
import com.werkstrom.distinfolab1.ui.ItemInfo;
import com.werkstrom.distinfolab1.ui.QuantityItemInfo;
import com.werkstrom.distinfolab1.ui.ShoppingCartInfo;

import java.util.ArrayList;
import java.util.List;

public final class ShoppingCartFacade {

    private ShoppingCartFacade() {

    }

    public static ShoppingCartInfo getCart(int userId) throws ConnectionException, QueryException {
        ensureConnected();
        MySqlShoppingCart cart = MySqlShoppingCart.getCart(userId);
        return toCartInfo(cart);
    }

    public static ShoppingCartInfo add(int userId, int itemId, int qty) throws ConnectionException, QueryException {
        ensureConnected();
        if (qty <= 0) throw new IllegalArgumentException("Quantity must be > 0");
        try {
            MySqlShoppingCart.addToCart(userId, itemId, qty);
        } catch (QueryException e) {
            // Försök öka om raden redan finns
            MySqlShoppingCart.addQuantityToCart(userId, itemId, qty);
        }
        return getCart(userId);
    }

    private static void ensureConnected() throws ConnectionException {
        if (!MySqlConnectionManager.isConnected()) {
            throw new ConnectionException("No database connection. Initialize connection before calling ItemFacade methods.");
        }
    }

    public static void add(int userId, int itemId, int qty) throws ConnectionException, QueryException{
        ensureConnected();
        if (qty < 0) {
            throw new IllegalArgumentException("Quantity must be > 0");
        }

    }



    private static ShoppingCartInfo toCartInfo(ShoppingCart cart) {
        List<QuantityItemInfo> out = new ArrayList<>();
        for (QuantityItem qi : cart.getCartItems()) {
            out.add(new QuantityItemInfo(toItemInfo(qi.getItem(), qi.getQuantity());
        }
        return new ShoppingCartInfo(cart.getOwnerId(), out);
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
