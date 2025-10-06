package com.werkstrom.distinfolab1.db;

import com.werkstrom.distinfolab1.bo.*;
import com.werkstrom.distinfolab1.bo.enums.OrderStatus;
import com.werkstrom.distinfolab1.bo.enums.UserRole;
import com.werkstrom.distinfolab1.db.exceptions.ConnectionException;
import com.werkstrom.distinfolab1.db.exceptions.NoResultException;
import com.werkstrom.distinfolab1.db.exceptions.QueryException;
import com.werkstrom.distinfolab1.db.exceptions.TransactionException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.*;
import java.util.Date;

public class MySqlUser extends User {

    private MySqlUser(int id, UserRole role, String name, String email, ShoppingCart cart, List<Order> orders) {
        super(id, role, name, email, cart, orders);
    }

    public static MySqlUser getUser(String email, String password) throws QueryException, ConnectionException, TransactionException {
        if (email == null || email.isEmpty()) throw new IllegalArgumentException("Email cannot be null or empty");
        if (password == null || password.isEmpty()) throw new IllegalArgumentException("Password cannot be null or empty");
        if (!MySqlConnectionManager.isConnected()) throw new ConnectionException("Connection is not established");

        email = email.trim().toLowerCase();
        password = password.trim();

        String query =
                        "SELECT " +
                        "    u.user_id, " +
                        "    u.name, " +
                        "    u.user_role " +
                        "FROM " +
                        "    User u " +
                        "WHERE " +
                        "    u.email = ? " +
                        "    AND u.password_hash = ?; " +
                        " " +
                        "SELECT " +
                        "    co.order_id, " +
                        "    co.status, " +
                        "    co.order_date, " +
                        "    oim.item_id, " +
                        "    oim.quantity, " +
                        "    i.name AS item_name, " +
                        "    i.description, " +
                        "    i.price, " +
                        "    icm.item_category_id, " +
                        "    ic.name AS item_category_name " +
                        "FROM " +
                        "    User u " +
                        "        LEFT JOIN Customer_order co ON u.user_id = co.user_id " +
                        "        LEFT JOIN Order_item_mapping oim ON co.order_id = oim.order_id " +
                        "        LEFT JOIN Item i ON oim.item_id = i.item_id " +
                        "        LEFT JOIN Item_category_mapping icm ON i.item_id = icm.item_id " +
                        "        LEFT JOIN Item_category ic ON icm.item_category_id = ic.item_category_id " +
                        "WHERE " +
                        "    u.email = ? " +
                        "    AND u.password_hash = ? " +
                        "ORDER BY " +
                        "    co.order_id, " +
                        "    oim.item_id, " +
                        "    icm.item_category_id; " +
                        " " +
                        "SELECT " +
                        "    sc.item_id, " +
                        "    sc.quantity, " +
                        "    i.name AS item_name, " +
                        "    i.description, " +
                        "    i.price, " +
                        "    i.stock, " +
                        "    icm.item_category_id, " +
                        "    ic.name AS item_category_name " +
                        "FROM " +
                        "    User u " +
                        "        LEFT JOIN Shopping_cart sc ON u.user_id = sc.user_id " +
                        "        LEFT JOIN Item i ON sc.item_id = i.item_id " +
                        "        LEFT JOIN Item_category_mapping icm ON i.item_id = icm.item_id " +
                        "        LEFT JOIN Item_category ic ON icm.item_category_id = ic.item_category_id " +
                        "WHERE " +
                        "    u.email = ? " +
                        "    AND u.password_hash = ? " +
                        "ORDER BY " +
                        "    sc.user_id, " +
                        "    i.item_id, " +
                        "    icm.item_category_id; ";

        try (PreparedStatement statement = MySqlConnectionManager.createPreparedStatement(query)) {
            MySqlConnectionManager.startTransaction();
            String password_hash = hashPassword(password);
            statement.setString(1, email);
            statement.setString(2, password_hash);
            statement.setString(3, email);
            statement.setString(4, password_hash);
            statement.setString(5, email);
            statement.setString(6, password_hash);
            boolean hasResults = statement.execute();
            if (!hasResults)
                throw new NoResultException("No user found with email: " + email + " and password: " + password);

            ResultSet resultSet = statement.getResultSet();
            resultSet.next();
            int userId = resultSet.getInt("user_id");
            String userName = resultSet.getString("name");
            UserRole role = UserRole.valueOf(resultSet.getString("user_role").toUpperCase());

            statement.getMoreResults();
            resultSet = statement.getResultSet();
            ArrayList<Order> orders = new ArrayList<>();
            int lastOrderId = 0;
            int lastItemId = 0;
            int lastCategoryId = 0;
            while (resultSet.next()) {
                int orderId = resultSet.getInt("order_id");
                if (orderId == 0) break;
                if (orderId != lastOrderId) {
                    OrderStatus orderStatus = OrderStatus.valueOf(resultSet.getString("status").toUpperCase());
                    Date orderDate = resultSet.getDate("order_date");
                    orders.add(new Order(orderId, userId, orderDate, null, orderStatus));
                    lastOrderId = orderId;
                    lastItemId = 0;
                }

                int itemId = resultSet.getInt("item_id");
                Order currentOrder = orders.get(orders.size() - 1);
                if (itemId != lastItemId) {
                    String itemName = resultSet.getString("item_name");
                    String description = resultSet.getString("description");
                    float price = resultSet.getFloat("price");
                    int quantity = resultSet.getInt("quantity");
                    currentOrder.addItem(new Item(itemId, itemName, description, price, 0, null), quantity);
                    lastItemId = itemId;
                    lastCategoryId = 0;
                }

                int categoryId = resultSet.getInt("item_category_id");
                if (categoryId != lastCategoryId) {
                    Item currentItem = currentOrder.getOrderItems().get(currentOrder.getNrOfItems() - 1).getItem();
                    String categoryName = resultSet.getString("item_category_name");
                    currentItem.addCategory(new ItemCategory(categoryId, categoryName));
                    lastCategoryId = categoryId;
                }
            }

            statement.getMoreResults();
            resultSet = statement.getResultSet();
            lastItemId = 0;
            lastCategoryId = 0;
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

            return new MySqlUser(
                    userId,
                    role,
                    userName,
                    email,
                    new ShoppingCart(userId, cartItems),
                    orders
            );
        }
        catch (ConnectionException e) {
            throw new ConnectionException("A connection needs to be established before user information can be queried: " + e.getMessage());
        }
        catch (QueryException e) {
            throw new QueryException("Could not retrieve user information: " + e.getMessage());
        }
        catch (SQLException e) {
            throw new QueryException("Failure while trying to prepare statement: " + e.getMessage());
        }
        catch (IllegalArgumentException e) {
            throw new QueryException("Illegal user role found in database: " + e.getMessage());
        }
    }

    public static void addUser(UserRole role, String userName, String email, String password) {
        if (role == null) throw new IllegalArgumentException("Role cannot be null");
        if (role == UserRole.GUEST)  throw new IllegalArgumentException("User role cannot be guest");
        if (userName == null || userName.isEmpty()) throw new IllegalArgumentException("Username cannot be null or empty");
        if (email == null || email.isEmpty()) throw new IllegalArgumentException("Email cannot be null or empty");
        if (password == null || password.isEmpty()) throw new IllegalArgumentException("Password cannot be null or empty");
        if (!MySqlConnectionManager.isConnected()) throw new ConnectionException("Connection is not established");

        userName = userName.trim();
        email = email.trim().toLowerCase();
        password = password.trim();

        String query =
                "INSERT INTO User (user_role, name, email, password_hash) " +
                "VALUES (?, ?, ?, ?);";

        try (PreparedStatement statement = MySqlConnectionManager.createPreparedStatement(query)) {
            statement.setString(1, role.getRoleName());
            statement.setString(2, userName);
            statement.setString(3, email);
            statement.setString(4, hashPassword(password));
            statement.executeUpdate();
        }
        catch (SQLException e) {
            throw new QueryException("Could not add user information: " + e.getMessage());
        }

    }

    public static void updateUserRole(int userId, UserRole role) throws ConnectionException, QueryException {
        if (role == null) throw new IllegalArgumentException("Role cannot be null");
        if (userId <= 0) throw new IllegalArgumentException("UserId cannot be negative or zero");
        if (role == UserRole.GUEST) throw new IllegalArgumentException("User cannot have role guest");
        if (!MySqlConnectionManager.isConnected()) throw new ConnectionException("Connection is not established");

        String query = "UPDATE User u SET u.user_role = ? WHERE u.user_id = ?;";
        try (PreparedStatement statement = MySqlConnectionManager.createPreparedStatement(query)) {
            statement.setString(1, role.getRoleName());
            statement.setInt(2, userId);
            statement.executeUpdate();
        }
        catch (SQLException e) {
            throw new QueryException("Could not update user role: " + e.getMessage());
        }
    }

    public static void deleteUser(int userId) throws ConnectionException, QueryException {
        if (userId <= 0) throw new IllegalArgumentException("UserId cannot be negative or zero");
        if (!MySqlConnectionManager.isConnected()) throw new ConnectionException("Connection is not established");

        String query = "DELETE FROM User u WHERE u.user_id = ?;";
        try (PreparedStatement statement = MySqlConnectionManager.createPreparedStatement(query)) {
            MySqlConnectionManager.startTransaction();
            statement.setInt(1, userId);
            statement.executeUpdate();
            MySqlConnectionManager.commitTransaction();
        }
        catch (SQLException e) {
            MySqlConnectionManager.rollbackTransaction();
            throw new QueryException("Could not delete user: " + e.getMessage());
        }
    }

    private static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString().toUpperCase();
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return "MySqlUser{" +
                "id=" + getId() +
                ", role=" + getRole() +
                ", name='" + getName() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", cart=" + getCart() +
                ", orders=" + getOrders() +
                '}';
    }
}
