package com.werkstrom.distinfolab1.db;

import com.werkstrom.distinfolab1.bo.*;
import com.werkstrom.distinfolab1.bo.enums.OrderStatus;
import com.werkstrom.distinfolab1.db.exceptions.QueryException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class MySqlOrder extends Order {
    MySqlOrder(int orderId, int ownerId, Date orderDate, List<QuantityItem> items, OrderStatus status) throws QueryException {
        super(orderId, ownerId, orderDate, items, status);
    }

    public static MySqlOrder createOrderFromCart(int userId) {
        if (userId <= 0) throw new IllegalArgumentException("userId cannot be negative or 0");

        String query =
                "SET @uid = ?; " +
                " " +
                "INSERT INTO Customer_order (user_id, status) " +
                "VALUES (@uid, 'ordered'); " +
                " " +
                "SET @order_id = LAST_INSERT_ID(); " +
                " " +
                "INSERT INTO Order_item_mapping (order_id, item_id, quantity) " +
                "SELECT @order_id, sc.item_id, sc.quantity " +
                "FROM Shopping_cart sc " +
                "WHERE sc.user_id = @uid; " +
                " " +
                "DELETE FROM Shopping_cart " +
                "WHERE user_id = @uid; " +
                " " +
                "SELECT " +
                "    co.order_id " +
                "    , co.order_date " +
                "    , oim.item_id " +
                "    , oim.quantity " +
                "    , i.name AS item_name " +
                "    , i.description " +
                "    , i.price " +
                "    , icm.item_category_id " +
                "    , ic.name AS item_category_name " +
                "FROM " +
                "    Customer_order co " +
                "LEFT JOIN " +
                "        Order_item_mapping oim ON " +
                "            co.order_id = oim.order_id " +
                "LEFT JOIN " +
                "        Item i ON " +
                "            oim.item_id = i.item_id " +
                "LEFT JOIN " +
                "        Item_category_mapping icm ON " +
                "            i.item_id = icm.item_id " +
                "LEFT JOIN " +
                "        Item_category ic ON " +
                "            icm.item_category_id = ic.item_category_id " +
                "WHERE " +
                "    co.order_id = @order_id " +
                "ORDER BY " +
                "    co.order_id " +
                "    , oim.item_id " +
                "    , icm.item_category_id;";

        try (PreparedStatement statement = MySqlConnectionManager.createPreparedStatement(query)) {
            statement.setInt(1, userId);
            MySqlConnectionManager.startTransaction();
            boolean hasResults = statement.execute();
            ResultSet resultSet = statement.getResultSet();
            int maxExpectedResultLoops = 5;
            int i = 0;
            while (!hasResults && i < maxExpectedResultLoops) {
                hasResults = statement.getMoreResults();
                resultSet =  statement.getResultSet();
                i++;
            }
            if (!hasResults && i == maxExpectedResultLoops) {
                MySqlConnectionManager.rollbackTransaction();
                throw new QueryException("Could not create order:  " + userId);
            }
            resultSet.next();

            int orderId = resultSet.getInt("order_id");
            Date orderDate = resultSet.getDate("order_date");
            Order newOrder = new Order(orderId, userId, orderDate, null, OrderStatus.ORDERED);

            int lastItemId = 0;
            int lastCategoryId = 0;
            do {
                int itemId = resultSet.getInt("item_id");
                if (itemId != lastItemId) {
                    String itemName = resultSet.getString("item_name");
                    String description = resultSet.getString("description");
                    float price = resultSet.getFloat("price");
                    int quantity = resultSet.getInt("quantity");
                    newOrder.addItem(new Item(itemId, itemName, description, price, 0, null), quantity);
                    lastItemId = itemId;
                    lastCategoryId = 0;
                }

                int categoryId = resultSet.getInt("item_category_id");
                if (categoryId != lastCategoryId) {
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

    public static void updateOrderStatus(int orderId, OrderStatus orderStatus) {
        if (orderStatus == null) throw new IllegalArgumentException("OrderStatus cannot be null");
        if (orderId <= 0) throw new IllegalArgumentException("OrderId cannot be negative or zero");

        String query = "UPDATE Customer_order co SET co.status = ? WHERE co.order_id = ?;";
        try (PreparedStatement statement = MySqlConnectionManager.createPreparedStatement(query)) {
            statement.setString(1, orderStatus.getStatusName());
            statement.setInt(2, orderId);
            statement.executeUpdate();
        }
        catch (SQLException e) {
            throw new QueryException("Could not update order status: " + e.getMessage());
        }
    }

}
