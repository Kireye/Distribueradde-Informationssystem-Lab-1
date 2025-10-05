package com.werkstrom.distinfolab1.db;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MySqlOrderTest {

    @Test
    void createOrderFromCart() {
        MySqlConnectionManager.initializeConnection("root", "MySQLRoot");
        MySqlOrder order = MySqlOrder.createOrderFromCart(3);
        assertNotNull(order);
        System.out.println(order);
    }
}