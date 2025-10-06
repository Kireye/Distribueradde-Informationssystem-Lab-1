package com.werkstrom.distinfolab1.db;

import com.werkstrom.distinfolab1.bo.QuantityItem;
import com.werkstrom.distinfolab1.bo.ShoppingCart;
import com.werkstrom.distinfolab1.db.exceptions.ConnectionException;
import com.werkstrom.distinfolab1.db.exceptions.QueryException;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class MySqlShoppingCart extends ShoppingCart {
    public MySqlShoppingCart(int ownerId, List<QuantityItem> items) {
        super(ownerId, items);
    }

    public static void addToCart(int userId, int itemId, int quantity) throws QueryException, ConnectionException {
        if (userId <= 0) throw new IllegalArgumentException("User id must be greater than zero");
        if (quantity <= 0) throw new IllegalArgumentException("Quantity must be greater than zero");
        if (itemId <= 0) throw new IllegalArgumentException("Item id must be greater than zero");
        if (!MySqlConnectionManager.isConnected()) throw new ConnectionException("No connection established");

        String query = "INSERT INTO Shopping_cart (user_id, item_id, quantity) VALUES (?, ?, ?);";
        try (PreparedStatement statement = MySqlConnectionManager.createPreparedStatement(query)) {
            statement.setInt(1, userId);
            statement.setInt(2, itemId);
            statement.setInt(3, quantity);
            statement.executeUpdate();
        }
        catch (SQLException e) {
            throw new QueryException("Could not add item to cart: " + e.getMessage());
        }
    }

    public static void addQuantityToCart(int userId, int itemId, int quantity) throws QueryException, ConnectionException {
        if (userId <= 0) throw new IllegalArgumentException("User id must be greater than zero");
        if (quantity <= 0) throw new IllegalArgumentException("Quantity must be greater than zero");
        if (itemId <= 0) throw new IllegalArgumentException("Item id must be greater than zero");
        if (!MySqlConnectionManager.isConnected()) throw new ConnectionException("No connection established");

        String query = "UPDATE Shopping_cart SET quantity = quantity + ? WHERE user_id = ? AND item_id = ?;";
        try (PreparedStatement statement = MySqlConnectionManager.createPreparedStatement(query)) {
            statement.setInt(1, quantity);
            statement.setInt(2, userId);
            statement.setInt(3, itemId);
            statement.executeUpdate();
        }
        catch (SQLException e) {
            throw new QueryException("Could not remove item from cart: " + e.getMessage());
        }
    }

    public static void removeFromCart(int userId, int itemId) throws QueryException, ConnectionException {
        if (userId <= 0) throw new IllegalArgumentException("User id must be greater than zero");
        if (itemId <= 0) throw new IllegalArgumentException("Item id must be greater than zero");
        if (!MySqlConnectionManager.isConnected()) throw new ConnectionException("No connection established");

        String query = "DELETE FROM Shopping_cart WHERE user_id = ? AND item_id = ?;";
        try(PreparedStatement statement = MySqlConnectionManager.createPreparedStatement(query)) {
            statement.setInt(1, userId);
            statement.setInt(2, itemId);
            statement.executeUpdate();
        }
        catch (SQLException e) {
            throw new QueryException("Could not remove item from cart: " + e.getMessage());
        }
    }

    public static void removeQuantityFromCart(int userId, int itemId, int quantity) throws QueryException, ConnectionException {
        if (userId <= 0) throw new IllegalArgumentException("User id must be greater than zero");
        if (quantity <= 0) throw new IllegalArgumentException("Quantity must be greater than zero");
        if (itemId <= 0) throw new IllegalArgumentException("Item id must be greater than zero");
        if (!MySqlConnectionManager.isConnected()) throw new ConnectionException("No connection established");

        String query = "UPDATE Shopping_cart SET quantity = quantity - ? WHERE user_id = ? AND item_id = ?;";
        try (PreparedStatement statement = MySqlConnectionManager.createPreparedStatement(query)) {
            statement.setInt(1, quantity);
            statement.setInt(2, userId);
            statement.setInt(3, itemId);
            statement.executeUpdate();
        }
        catch (SQLException e) {
            throw new QueryException("Could not remove item from cart: " + e.getMessage());
        }
    }

    public static void emptyCart(int userId)  throws QueryException, ConnectionException {
        if (userId <= 0) throw new IllegalArgumentException("User id must be greater than zero");
        if (!MySqlConnectionManager.isConnected()) throw new ConnectionException("No connection established");

        String query = "DELETE FROM Shopping_cart WHERE user_id = ?;";
        MySqlConnectionManager.startTransaction();
        try (PreparedStatement statement = MySqlConnectionManager.createPreparedStatement(query)) {
            statement.setInt(1, userId);
            statement.executeUpdate();
            MySqlConnectionManager.commitTransaction();
        }
        catch (Exception e) {
            MySqlConnectionManager.rollbackTransaction();
            throw new QueryException("Could not empty cart of user " + userId + " : " + e.getMessage());
        }
    }
}
