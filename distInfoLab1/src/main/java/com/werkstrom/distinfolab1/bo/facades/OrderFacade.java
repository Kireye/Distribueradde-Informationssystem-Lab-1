package com.werkstrom.distinfolab1.bo.facades;

import com.werkstrom.distinfolab1.bo.Item;
import com.werkstrom.distinfolab1.bo.QuantityItem;
import com.werkstrom.distinfolab1.bo.enums.OrderStatus;
import com.werkstrom.distinfolab1.db.MySqlConnectionManager;
import com.werkstrom.distinfolab1.db.MySqlOrder;
import com.werkstrom.distinfolab1.db.exceptions.ConnectionException;
import com.werkstrom.distinfolab1.db.exceptions.NoResultException;
import com.werkstrom.distinfolab1.db.exceptions.QueryException;
import com.werkstrom.distinfolab1.ui.ItemInfo;
import com.werkstrom.distinfolab1.ui.OrderInfo;
import com.werkstrom.distinfolab1.ui.OrderLineInfo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public final class OrderFacade {

    private OrderFacade() { }

    public static OrderInfo placeOrder(int userId) throws ConnectionException, QueryException {
        ensureConnected();
        MySqlOrder dbOrder = MySqlOrder.createOrderFromCart(userId);
        return toOrderInfo(dbOrder);
    }

    public static List<OrderInfo> listOrders(int userId) throws ConnectionException, QueryException {
        ensureConnected();
        List<MySqlOrder> orders = MySqlOrder.getOrdersByUser(userId);
        List<OrderInfo> out = new ArrayList<>();
        for (MySqlOrder o : orders) out.add(toOrderInfo(o));
        return out;
    }

    public static OrderInfo getOrder(int userId, int orderId) throws ConnectionException, QueryException, NoResultException {
        ensureConnected();
        MySqlOrder o = MySqlOrder.getOrderForUser(userId, orderId);
        return toOrderInfo(o);
    }

    private static void ensureConnected() throws ConnectionException {
        if (!MySqlConnectionManager.isConnected())
            throw new ConnectionException("No database connection.");
    }

    private static OrderInfo toOrderInfo(MySqlOrder o) {
        int orderId = o.getOrderId();
        int ownerId = o.getOwnerId();
        Date orderDate = o.getOrderDate();
        OrderStatus status = o.getStatus();

        List<OrderLineInfo> lines = new ArrayList<>();
        for (QuantityItem qi : o.getOrderItems()) {
            Item it = qi.getItem();
            ItemInfo ii = new ItemInfo(it.getId(), it.getName(), it.getDescription(), it.getPrice(), it.getStock(), it.getCategories());
            lines.add(new OrderLineInfo(ii, qi.getQuantity()));
        }

        return new OrderInfo(orderId, ownerId, orderDate, lines, status);
    }
}
