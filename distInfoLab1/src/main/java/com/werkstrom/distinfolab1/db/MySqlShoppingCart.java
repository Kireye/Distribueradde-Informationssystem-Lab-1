package com.werkstrom.distinfolab1.db;

import com.werkstrom.distinfolab1.bo.Item;
import com.werkstrom.distinfolab1.bo.ItemCategory;
import com.werkstrom.distinfolab1.bo.QuantityItem;
import com.werkstrom.distinfolab1.bo.ShoppingCart;
import com.werkstrom.distinfolab1.db.exceptions.ConnectionException;
import com.werkstrom.distinfolab1.db.exceptions.NoResultException;
import com.werkstrom.distinfolab1.db.exceptions.QueryException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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

    public static MySqlShoppingCart getShoppingCart(int userId) throws QueryException, ConnectionException {
        if (userId <= 0) throw new IllegalArgumentException("User id must be greater than zero");
        if (!MySqlConnectionManager.isConnected()) throw new ConnectionException("No connection established");

        String query =
                "SELECT " +
                        "    sc.item_id " +
                        "    , sc.quantity " +
                        "    , i.name AS item_name " +
                        "    , i.description " +
                        "    , i.price " +
                        "    , i.stock " +
                        "    , icm.item_category_id " +
                        "    , ic.name AS item_category_name " +
                        "FROM " +
                        "    Shopping_cart sc " +
                        "LEFT JOIN " +
                        "        Item i ON " +
                        "            sc.item_id = i.item_id " +
                        "LEFT JOIN " +
                        "        Item_category_mapping icm ON " +
                        "            i.item_id = icm.item_id " +
                        "LEFT JOIN " +
                        "        Item_category ic ON " +
                        "            icm.item_category_id = ic.item_category_id " +
                        "WHERE  " +
                        "    sc.user_id = ?;";
        try (PreparedStatement statement = MySqlConnectionManager.createPreparedStatement(query)) {
            statement.setInt(1, userId);
            boolean hasResult = statement.execute();
            if (!hasResult) throw new NoResultException("Could not get cart of user " + userId);
            ResultSet resultSet = statement.getResultSet();
            int lastItemId = 0;
            int lastCategoryId = 0;
            ArrayList<QuantityItem> cartItems = new ArrayList<>();
            while (resultSet.next()) {
                int itemId = resultSet.getInt("item_id");
                if (itemId == 0) break;
                if (itemId != lastItemId) {
                    String itemName = resultSet.getString("item_name");
                    String description = resultSet.getString("description");
                    float price = resultSet.getFloat("price");
                    int stock = resultSet.getInt("stock");
                    int quantity = resultSet.getInt("quantity");
                    Item newItem = new Item(itemId, itemName, description, price, stock, null);
                    cartItems.add(new QuantityItem(quantity, newItem));
                    lastItemId = itemId;
                    lastCategoryId = 0;
                }

                int categoryId = resultSet.getInt("item_category_id");
                if (categoryId != lastCategoryId) {
                    Item currentItem = cartItems.get(cartItems.size() - 1).getItem();
                    String categoryName = resultSet.getString("item_category_name");
                    currentItem.addCategory(new ItemCategory(categoryId, categoryName));
                    lastCategoryId = categoryId;
                }
            }
            return new MySqlShoppingCart(userId, cartItems);
        }
        catch (SQLException e) {
            throw new QueryException("Could not get cart of user: " + e.getMessage());
        }
    }
}
