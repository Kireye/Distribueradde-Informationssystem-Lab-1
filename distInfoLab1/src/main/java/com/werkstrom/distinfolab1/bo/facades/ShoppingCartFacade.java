package com.werkstrom.distinfolab1.bo.facades;

import com.werkstrom.distinfolab1.bo.QuantityItem;
import com.werkstrom.distinfolab1.bo.ShoppingCart;
import com.werkstrom.distinfolab1.db.MySqlConnectionManager;
import com.werkstrom.distinfolab1.db.MySqlShoppingCart;
import com.werkstrom.distinfolab1.db.exceptions.ConnectionException;
import com.werkstrom.distinfolab1.db.exceptions.QueryException;
import com.werkstrom.distinfolab1.ui.CartItemInfo;
import com.werkstrom.distinfolab1.ui.ShoppingCartInfo;

import java.util.ArrayList;
import java.util.List;

public final class ShoppingCartFacade {

    private ShoppingCartFacade() { }

    public static ShoppingCartInfo getCart(int userId) throws ConnectionException, QueryException {
        ensureConnected();
        ShoppingCart cart = MySqlShoppingCart.getCart(userId);
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

    public static ShoppingCartInfo updateQuantity(int userId, int itemId, int qty) throws ConnectionException, QueryException {
        ensureConnected();
        if (qty <= 0) {
            MySqlShoppingCart.removeFromCart(userId, itemId);
        } else {
            // Sätt kvantitet exakt: enklast genom att läsa nuvarande, räkna delta
            ShoppingCart current = MySqlShoppingCart.getCart(userId);
            int currentQty = 0;
            for (QuantityItem qi : current.getCartItems()) {
                if (qi.getItem().getId() == itemId) { currentQty = qi.getQuantity(); break; }
            }
            int delta = qty - currentQty;
            if (delta > 0) MySqlShoppingCart.addQuantityToCart(userId, itemId, delta);
            else if (delta < 0) MySqlShoppingCart.removeQuantityFromCart(userId, itemId, -delta);
        }
        return getCart(userId);
    }

    public static ShoppingCartInfo remove(int userId, int itemId) throws ConnectionException, QueryException {
        ensureConnected();
        MySqlShoppingCart.removeFromCart(userId, itemId);
        return getCart(userId);
    }

    public static ShoppingCartInfo clear(int userId) throws ConnectionException, QueryException {
        ensureConnected();
        MySqlShoppingCart.emptyCart(userId);
        return getCart(userId);
    }

    private static void ensureConnected() throws ConnectionException {
        if (!MySqlConnectionManager.isConnected())
            throw new ConnectionException("No database connection.");
    }

    private static ShoppingCartInfo toCartInfo(ShoppingCart cart) {
        List<CartItemInfo> out = new ArrayList<>();
        for (QuantityItem qi : cart.getCartItems()) {
            out.add(new CartItemInfo(
                    qi.getItem().getId(),
                    qi.getItem().getName(),
                    qi.getItem().getPrice(),
                    qi.getItem().getStock(),
                    qi.getQuantity(),
                    qi.getItem().getCategories()
            ));
        }
        return new ShoppingCartInfo(cart.getOwnerId(), out);
    }
}