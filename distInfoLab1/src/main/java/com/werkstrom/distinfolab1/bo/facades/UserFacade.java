package com.werkstrom.distinfolab1.bo.facades;

import com.werkstrom.distinfolab1.bo.CartItem;
import com.werkstrom.distinfolab1.bo.Item;
import com.werkstrom.distinfolab1.bo.ShoppingCart;
import com.werkstrom.distinfolab1.db.MySqlConnectionManager;
import com.werkstrom.distinfolab1.db.MySqlUser;
import com.werkstrom.distinfolab1.db.exceptions.ConnectionException;
import com.werkstrom.distinfolab1.db.exceptions.QueryException;
import com.werkstrom.distinfolab1.db.exceptions.TransactionException;
import com.werkstrom.distinfolab1.ui.CartItemInfo;
import com.werkstrom.distinfolab1.ui.ItemInfo;
import com.werkstrom.distinfolab1.ui.ShoppingCartInfo;
import com.werkstrom.distinfolab1.ui.UserInfo;

import java.util.ArrayList;
import java.util.List;

public final class UserFacade {

    private UserFacade() {

    }

    public static UserInfo login(String email, String password) throws ConnectionException, QueryException, TransactionException {
        if (email == null) {
            throw new IllegalArgumentException("email cannot be null");
        }
        if (password == null) {
            throw new IllegalArgumentException("password cannot be null");
        }

        if (email.isEmpty()) {
            throw new IllegalArgumentException("email cannot be empty");
        }
        if (password.isEmpty()) {
            throw new IllegalArgumentException("password cannot be empty");
        }

        if (!MySqlConnectionManager.isConnected()) {
            throw new ConnectionException("No database connection. Initialize connection before calling login.");
        }

        MySqlUser user = MySqlUser.getUser(email, password);

        MySqlConnectionManager.closeConnection();

        MySqlConnectionManager.initializeConnection(user.getRole().getRoleName(), user.getRole().getRoleName());

        System.out.println(user.getRole().getRoleName());

        return new UserInfo(
                user.getId(),
                user.getRole(),
                user.getName(),
                user.getEmail(),
                user.getCart(),
                user.getOrders()
        );
    }

    public static void logout() {
        if (!MySqlConnectionManager.isConnected()) {
            throw new ConnectionException("No database connection. Initialize connection before calling logout.");
        }
        MySqlConnectionManager.closeConnection();
        MySqlConnectionManager.initializeConnection("guest", "guest");
        System.out.println("guest");
    }

    public static ShoppingCartInfo getShoppingCart(int userId) throws ConnectionException, QueryException {
        if (userId <= 0) throw new IllegalArgumentException("userId must be greater than 0");
        if (!MySqlConnectionManager.isConnected()) throw new ConnectionException("No database connection.");

        ShoppingCart cart = MySqlUser.getCart(userId);
        List<CartItemInfo> lines = new ArrayList<>();

        for (CartItem ci : cart.getCartItems()) {
            Item it = ci.getItem();
            ItemInfo ii = new ItemInfo(
                    it.getId(),
                    it.getName(),
                    it.getDescription(),
                    it.getPrice(),
                    it.getStock(),
                    it.getCategories()
            );
            lines.add(new CartItemInfo(ci.getQuantity(), ii));
        }

        return new ShoppingCartInfo(cart.getOwnerId(), lines);
    }

    public static void addToCart(int userId, int itemId, int quantity) throws ConnectionException, QueryException {
        if (userId <= 0) {
            throw new IllegalArgumentException("userId must be greater than 0");
        }
        if (itemId <= 0) {
            throw new IllegalArgumentException("itemId must be greater than 0");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("quantity must be greater than 0");
        }

        if (!MySqlConnectionManager.isConnected()) {
            throw new ConnectionException("No database connection.");
        }

        MySqlUser.addToCart(userId, itemId, quantity);
    }

    public static void addQuantityToCart(int userId, int itemId, int quantity) throws ConnectionException, QueryException {
        if (userId <= 0) {
            throw new IllegalArgumentException("userId must be greater than 0");
        }
        if (itemId <= 0) {
            throw new IllegalArgumentException("itemId must be greater than 0");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("quantity must be greater than 0");
        }

        if (!MySqlConnectionManager.isConnected()) {
            throw new ConnectionException("No database connection.");
        }

        MySqlUser.addQuantityToCart(userId, itemId, quantity);
    }

    public static void removeQuantityFromCart(int userId, int itemId, int quantity) throws ConnectionException, QueryException {
        if (userId <= 0) {
            throw new IllegalArgumentException("userId must be greater than 0");
        }
        if (itemId <= 0) {
            throw new IllegalArgumentException("itemId must be greater than 0");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("quantity must be greater than 0");
        }

        if (!MySqlConnectionManager.isConnected()) {
            throw new ConnectionException("No database connection.");
        }

        MySqlUser.removeQuantityFromCart(userId, itemId, quantity);
    }

    public static void removeFromCart(int userId, int itemId) throws ConnectionException, QueryException {
        if (userId <= 0) {
            throw new IllegalArgumentException("userId must be greater than 0");
        }
        if (itemId <= 0) {
            throw new IllegalArgumentException("itemId must be greater than 0");
        }

        if (!MySqlConnectionManager.isConnected()) {
            throw new ConnectionException("No database connection.");
        }

        MySqlUser.removeFromCart(userId, itemId);
    }

    public static void emptyCart(int userId) throws ConnectionException, QueryException {
        if (userId <= 0) {
            throw new IllegalArgumentException("userId must be greater than 0");
        }

        if (!MySqlConnectionManager.isConnected()) {
            throw new ConnectionException("No database connection.");
        }

        MySqlUser.emptyCart(userId);
    }

    public static UserInfo reloadByCredentials(String email, String password) throws ConnectionException, QueryException, TransactionException {
        return login(email, password);
    }
}