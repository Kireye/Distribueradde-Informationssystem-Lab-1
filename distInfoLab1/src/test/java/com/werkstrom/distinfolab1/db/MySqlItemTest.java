package com.werkstrom.distinfolab1.db;

import com.werkstrom.distinfolab1.bo.ItemCategory;
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

    @Test
    void getItemsByName() {
        MySqlConnectionManager.initializeConnection("root", "MySQLRoot");
        ArrayList<MySqlItem> result = (ArrayList<MySqlItem>) MySqlItem.getItemsByName("0", false);
        for (MySqlItem item : result) {
            System.out.println(item);
        }
    }

    @Test
    void getItemsByCategory() {
        MySqlConnectionManager.initializeConnection("root", "MySQLRoot");
        ArrayList<MySqlItem> result = (ArrayList<MySqlItem>) MySqlItem.getItemsByCategory(3, false);
        for (MySqlItem item : result) {
            System.out.println(item);
        }
    }
}