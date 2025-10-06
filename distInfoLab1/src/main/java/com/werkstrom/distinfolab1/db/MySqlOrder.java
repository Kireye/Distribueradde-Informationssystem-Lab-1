package com.werkstrom.distinfolab1.db;

import com.werkstrom.distinfolab1.bo.*;
import com.werkstrom.distinfolab1.bo.enums.OrderStatus;
import com.werkstrom.distinfolab1.db.exceptions.ConnectionException;
import com.werkstrom.distinfolab1.db.exceptions.NoResultException;
import com.werkstrom.distinfolab1.db.exceptions.QueryException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class MySqlOrder extends Order {
    MySqlOrder(int orderId, int ownerId, Date orderDate, List<QuantityItem> items, OrderStatus status) {
        super(orderId, ownerId, orderDate, items, status);
    }

    public static MySqlOrder createOrderFromCart(int userId) {
        if (userId <= 0) throw new IllegalArgumentException("userId cannot be negative or 0");

        String query =
                "SET @uid = ?; " +
                        "INSERT INTO Customer_order (user_id, status) VALUES (@uid, 'ordered'); " +
                        "SET @order_id = LAST_INSERT_ID(); " +
                        "INSERT INTO Order_item_mapping (order_id, item_id, quantity) " +
                        "SELECT @order_id, sc.item_id, sc.quantity FROM Shopping_cart sc WHERE sc.user_id = @uid; " +
                        "DELETE FROM Shopping_cart WHERE user_id = @uid; " +
                        "SELECT " +
                        "  co.order_id, co.order_date, oim.item_id, oim.quantity, " +
                        "  i.name AS item_name, i.description, i.price, " +
                        "  icm.item_category_id, ic.name AS item_category_name " +
                        "FROM Customer_order co " +
                        "LEFT JOIN Order_item_mapping oim ON co.order_id = oim.order_id " +
                        "LEFT JOIN Item i ON oim.item_id = i.item_id " +
                        "LEFT JOIN Item_category_mapping icm ON i.item_id = icm.item_id " +
                        "LEFT JOIN Item_category ic ON icm.item_category_id = ic.item_category_id " +
                        "WHERE co.order_id = @order_id " +
                        "ORDER BY co.order_id, oim.item_id, icm.item_category_id;";

        try (PreparedStatement statement = MySqlConnectionManager.createPreparedStatement(query)) {
            statement.setInt(1, userId);
            MySqlConnectionManager.startTransaction();
            boolean hasResults = statement.execute();
            ResultSet resultSet = statement.getResultSet();

            // Hoppa till SELECT-resultsetet
            int maxLoops = 6;
            int i = 0;
            while (!hasResults && i < maxLoops) {
                hasResults = statement.getMoreResults();
                resultSet = statement.getResultSet();
                i++;
            }
            if (!hasResults) {
                MySqlConnectionManager.rollbackTransaction();
                throw new QueryException("Could not create order for user " + userId);
            }

            resultSet.next();
            int orderId = resultSet.getInt("order_id");
            Date orderDate = resultSet.getTimestamp("order_date");
            Order newOrder = new Order(orderId, userId, orderDate, null, OrderStatus.ORDERED);

            int lastItemId = 0;
            int lastCategoryId = 0;
            do {
                int itemId = resultSet.getInt("item_id");
                if (itemId != lastItemId) {
                    String itemName = resultSet.getString("item_name");
                    String description = resultSet.getString("description");
                    float price = resultSet.getFloat("price");
                    int qty = resultSet.getInt("quantity");
                    newOrder.addItem(new Item(itemId, itemName, description, price, 0, null), qty);
                    lastItemId = itemId;
                    lastCategoryId = 0;
                }

                int categoryId = resultSet.getInt("item_category_id");
                if (categoryId != lastCategoryId && categoryId != 0) {
                    Item currentItem = newOrder.getOrderItems().get(newOrder.getNrOfItems() - 1).getItem();
                    String categoryName = resultSet.getString("item_category_name");
                    currentItem.addCategory(new ItemCategory(categoryId, categoryName));
                    lastCategoryId = categoryId;
                }
            } while (resultSet.next());

            MySqlConnectionManager.commitTransaction();
            return new MySqlOrder(
                    newOrder.getOrderId(),
                    newOrder.getOwnerId(),
                    newOrder.getOrderDate(),
                    newOrder.getOrderItems(),
                    OrderStatus.ORDERED
            );
        }
        catch (QueryException e) {
            throw new QueryException(e.getMessage());
        }
        catch (Exception e) {
            MySqlConnectionManager.rollbackTransaction();
            throw new QueryException("Failed to place order: " + e.getMessage());
        }
    }

    public static List<MySqlOrder> getOrdersByUser(int userId) throws ConnectionException, QueryException {
        if (userId <= 0) throw new IllegalArgumentException("userId must be > 0");
        if (!MySqlConnectionManager.isConnected()) throw new ConnectionException("No connection");

        String sql =
                "SELECT " +
                        "  co.order_id, co.status, co.order_date, " +
                        "  oim.item_id, oim.quantity, " +
                        "  i.name AS item_name, i.description, i.price, " +
                        "  icm.item_category_id, ic.name AS item_category_name " +
                        "FROM Customer_order co " +
                        "LEFT JOIN Order_item_mapping oim ON co.order_id = oim.order_id " +
                        "LEFT JOIN Item i ON oim.item_id = i.item_id " +
                        "LEFT JOIN Item_category_mapping icm ON i.item_id = icm.item_id " +
                        "LEFT JOIN Item_category ic ON icm.item_category_id = ic.item_category_id " +
                        "WHERE co.user_id = ? " +
                        "ORDER BY co.order_id, oim.item_id, icm.item_category_id;";

        try (PreparedStatement ps = MySqlConnectionManager.createPreparedStatement(sql)) {
            ps.setInt(1, userId);
            boolean has = ps.execute();
            if (!has) return new ArrayList<>();

            ResultSet rs = ps.getResultSet();
            List<MySqlOrder> orders = new ArrayList<>();
            int lastOrderId = 0;
            int lastItemId = 0;
            int lastCatId = 0;
            Order currentOrder = null;

            while (rs.next()) {
                int orderId = rs.getInt("order_id");
                if (orderId != lastOrderId) {
                    OrderStatus st = OrderStatus.valueOf(rs.getString("status").toUpperCase());
                    Date date = rs.getTimestamp("order_date");
                    currentOrder = new Order(orderId, userId, date, null, st);
                    orders.add(new MySqlOrder(orderId, userId, date, currentOrder.getOrderItems(), st));
                    lastOrderId = orderId;
                    lastItemId = 0;
                    lastCatId = 0;
                }

                int itemId = rs.getInt("item_id");
                if (itemId != 0 && itemId != lastItemId) {
                    String name = rs.getString("item_name");
                    String desc = rs.getString("description");
                    float price = rs.getFloat("price");
                    int qty = rs.getInt("quantity");
                    currentOrder.addItem(new Item(itemId, name, desc, price, 0, null), qty);
                    lastItemId = itemId;
                    lastCatId = 0;
                }

                int catId = rs.getInt("item_category_id");
                if (catId != 0 && catId != lastCatId && currentOrder != null && currentOrder.getNrOfItems() > 0) {
                    Item lastItem = currentOrder.getOrderItems().get(currentOrder.getNrOfItems() - 1).getItem();
                    String catName = rs.getString("item_category_name");
                    lastItem.addCategory(new ItemCategory(catId, catName));
                    lastCatId = catId;
                }
            }
            return orders;
        } catch (SQLException e) {
            throw new QueryException("Failed to load orders: " + e.getMessage());
        }
    }

    public static MySqlOrder getOrderForUser(int userId, int orderId) throws ConnectionException, QueryException, NoResultException {
        if (userId <= 0 || orderId <= 0) throw new IllegalArgumentException("ids must be > 0");
        if (!MySqlConnectionManager.isConnected()) throw new ConnectionException("No connection");

        String sql =
                "SELECT " +
                        "  co.order_id, co.status, co.order_date, " +
                        "  oim.item_id, oim.quantity, " +
                        "  i.name AS item_name, i.description, i.price, " +
                        "  icm.item_category_id, ic.name AS item_category_name " +
                        "FROM Customer_order co " +
                        "LEFT JOIN Order_item_mapping oim ON co.order_id = oim.order_id " +
                        "LEFT JOIN Item i ON oim.item_id = i.item_id " +
                        "LEFT JOIN Item_category_mapping icm ON i.item_id = icm.item_id " +
                        "LEFT JOIN Item_category ic ON icm.item_category_id = ic.item_category_id " +
                        "WHERE co.user_id = ? AND co.order_id = ? " +
                        "ORDER BY oim.item_id, icm.item_category_id;";

        try (PreparedStatement ps = MySqlConnectionManager.createPreparedStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, orderId);
            boolean has = ps.execute();
            if (!has) throw new NoResultException("Order not found");

            ResultSet rs = ps.getResultSet();
            if (!rs.next()) throw new NoResultException("Order not found");

            OrderStatus st = OrderStatus.valueOf(rs.getString("status").toUpperCase());
            Date date = rs.getTimestamp("order_date");
            Order order = new Order(orderId, userId, date, null, st);

            int lastItemId = 0;
            int lastCatId = 0;
            do {
                int itemId = rs.getInt("item_id");
                if (itemId != 0 && itemId != lastItemId) {
                    String name = rs.getString("item_name");
                    String desc = rs.getString("description");
                    float price = rs.getFloat("price");
                    int qty = rs.getInt("quantity");
                    order.addItem(new Item(itemId, name, desc, price, 0, null), qty);
                    lastItemId = itemId;
                    lastCatId = 0;
                }

                int catId = rs.getInt("item_category_id");
                if (catId != 0 && catId != lastCatId && order.getNrOfItems() > 0) {
                    Item lastItem = order.getOrderItems().get(order.getNrOfItems() - 1).getItem();
                    String catName = rs.getString("item_category_name");
                    lastItem.addCategory(new ItemCategory(catId, catName));
                    lastCatId = catId;
                }
            } while (rs.next());

            return new MySqlOrder(order.getOrderId(), order.getOwnerId(), order.getOrderDate(), order.getOrderItems(), st);
        } catch (SQLException e) {
            throw new QueryException("Failed to load order: " + e.getMessage());
        }
    }
}