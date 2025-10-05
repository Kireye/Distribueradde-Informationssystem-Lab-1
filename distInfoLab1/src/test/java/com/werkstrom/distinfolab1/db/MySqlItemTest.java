package com.werkstrom.distinfolab1.db;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class MySqlItemTest {

    @Test
    void getAllItems() {
        MySqlConnectionManager.initializeConnection("root", "MySQLRoot");
        ArrayList<MySqlItem> result = (ArrayList<MySqlItem>) MySqlItem.getAllItems(true);
        for (MySqlItem item : result) {
            System.out.println(item);
        }
    }
}