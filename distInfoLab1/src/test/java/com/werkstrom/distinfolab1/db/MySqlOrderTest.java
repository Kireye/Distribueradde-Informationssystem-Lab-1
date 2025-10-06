package com.werkstrom.distinfolab1.db;

import com.werkstrom.distinfolab1.bo.enums.OrderStatus;
import org.junit.jupiter.api.Test;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.*;

class MySqlOrderTest {

    @Test
    void createOrderFromCart() {
        MySqlConnectionManager.initializeConnection("root", "MySQLRoot");
        MySqlOrder order = MySqlOrder.createOrderFromCart(3);
        assertNotNull(order);
        System.out.println(order);
    }

    @Test
    void updateOrderStatus() {
        MySqlConnectionManager.initializeConnection("root", "MySQLRoot");
        MySqlOrder.updateOrderStatus(1, OrderStatus.PACKAGED);

        String query = "SELECT * FROM Customer_order WHERE order_id = ?";
        try (PreparedStatement statement = MySqlConnectionManager.createPreparedStatement(query)) {
            statement.setInt(1, 1);
            ResultSet rs = statement.executeQuery();
            assertTrue(rs.next());
            assertEquals(1, rs.getInt("order_id"));
            assertEquals(OrderStatus.PACKAGED.getStatusName(), rs.getString("status"));
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}