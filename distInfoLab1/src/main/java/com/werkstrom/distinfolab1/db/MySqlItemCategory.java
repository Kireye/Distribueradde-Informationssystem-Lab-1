package com.werkstrom.distinfolab1.db;

import com.werkstrom.distinfolab1.bo.ItemCategory;
import com.werkstrom.distinfolab1.db.exceptions.ConnectionException;
import com.werkstrom.distinfolab1.db.exceptions.NoResultException;
import com.werkstrom.distinfolab1.db.exceptions.QueryException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MySqlItemCategory extends ItemCategory {
    MySqlItemCategory(int id, String name) {
        super(id, name);
    }

    public static List<MySqlItemCategory> getAllItemCategories() throws IllegalArgumentException, ConnectionException, NoResultException {
        if (!MySqlConnectionManager.isConnected())  throw new ConnectionException("No connection to database");

        String query ="SELECT ic.item_category_id, ic.name FROM Item_category ic;";
        try (PreparedStatement statement = MySqlConnectionManager.createPreparedStatement(query)) {
            boolean hasResults = statement.execute();
            if (!hasResults)  throw new NoResultException("No item categories found");

            ResultSet resultSet = statement.getResultSet();
            ArrayList<ItemCategory> itemCategories = new ArrayList<>();
            while(resultSet.next()) {
                int id = resultSet.getInt("item_category_id");
                if (id == 0) throw new NoResultException("No item categories found");
                String name =  resultSet.getString("name");
                itemCategories.add(new ItemCategory(id, name));
            }

            ArrayList<MySqlItemCategory> categories = new ArrayList<>();
            for (ItemCategory itemCategory : itemCategories) {
                categories.add(new MySqlItemCategory(itemCategory.getId(), itemCategory.getName()));
            }
            return categories;
        }
        catch (SQLException e) {
            throw new QueryException("Failed to get item categories from database: " + e.getMessage());
        }
    }

}
