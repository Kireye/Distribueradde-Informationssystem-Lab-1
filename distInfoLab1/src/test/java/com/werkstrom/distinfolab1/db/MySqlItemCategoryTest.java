package com.werkstrom.distinfolab1.db;

import com.werkstrom.distinfolab1.bo.ItemCategory;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class MySqlItemCategoryTest {

    @Test
    void getAllItemCategories() {
        MySqlConnectionManager.initializeConnection("root", "MySQLRoot");
        ArrayList<MySqlItemCategory> result = (ArrayList<MySqlItemCategory>) MySqlItemCategory.getAllItemCategories();
        for (ItemCategory item : result) {
            System.out.println(item);
        }
    }

}