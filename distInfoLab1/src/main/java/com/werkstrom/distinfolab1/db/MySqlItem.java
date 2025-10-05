package com.werkstrom.distinfolab1.db;

import com.werkstrom.distinfolab1.bo.Item;
import com.werkstrom.distinfolab1.bo.ItemCategory;
import com.werkstrom.distinfolab1.db.exceptions.NoResultException;
import com.werkstrom.distinfolab1.db.exceptions.QueryException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MySqlItem extends Item {
    private MySqlItem(int id, String name, String description, float price, int stock, List<ItemCategory> categories) {
        super(id, name, description, price, stock, categories);
    }

    public static List<MySqlItem> getAllItems(boolean inStockOnly) {
        String query = 
                "SELECT " +
                "    i.item_id " +
                "    , i.name AS item_name " +
                "    , i.description " +
                "    , i.price " +
                "    , i.stock " +
                "    , icm.item_category_id " +
                "    , ic.name AS category_name " +
                "FROM " +
                     "Item i " +
                "LEFT JOIN " +
                "    Item_category_mapping icm ON " +
                "        i.item_id = icm.item_id " +
                "LEFT JOIN " +
                 "    Item_category ic ON " +
                 "        icm.item_category_id = ic.item_category_id " +
                 "WHERE " +
                 "    i.stock >= ? " +
                 "ORDER BY " +
                 "    i.item_id " +
                  "    , icm.item_category_id;";

        try (PreparedStatement statement = MySqlConnectionManager.createPreparedStatement(query)) {
            int stockCriteria = inStockOnly ? 1 : 0;
            statement.setInt(1, stockCriteria);
            boolean hasResult = statement.execute();
            if (!hasResult) throw new NoResultException("No items found");
            return getItemsFromResultSet(statement.getResultSet());
        }
        catch (SQLException e) {
            throw new QueryException("Failed to get items from database: " + e.getMessage());
        }
    }

    public static MySqlItem getItemById(int itemId) {
        if (itemId <= 0) {
            throw new IllegalArgumentException("itemId must be > 0");
        }

        String query =
                "SELECT " +
                        "    i.item_id, " +
                        "    i.name AS item_name, " +
                        "    i.description, " +
                        "    i.price, " +
                        "    i.stock, " +
                        "    icm.item_category_id, " +
                        "    ic.name AS category_name " +
                        "FROM Item i " +
                        "LEFT JOIN Item_category_mapping icm ON i.item_id = icm.item_id " +
                        "LEFT JOIN Item_category ic ON icm.item_category_id = ic.item_category_id " +
                        "WHERE i.item_id = ? " +
                        "ORDER BY icm.item_category_id;";

        try (PreparedStatement statement = MySqlConnectionManager.createPreparedStatement(query)) {
            statement.setInt(1, itemId);
            boolean hasResult = statement.execute();
            if (!hasResult) {
                throw new NoResultException("No item found with id " + itemId);
            }
            List<MySqlItem> list = getItemsFromResultSet(statement.getResultSet());
            if (list == null || list.isEmpty()) {
                throw new NoResultException("No item found with id " + itemId);
            }
            return list.get(0);
        }
        catch (SQLException e) {
            throw new QueryException("Failed to get item by id: " + e.getMessage());
        }
    }

    public static List<MySqlItem> getItemsByName(String searchTerm, boolean inStockOnly) {
        if (searchTerm == null || searchTerm.isEmpty()) throw new IllegalArgumentException("searchTerm cannot be null or empty");

        String query = "SELECT " +
                "    i.item_id " +
                "    , i.name AS item_name " +
                "    , i.description " +
                "    , i.price " +
                "    , i.stock " +
                "    , icm.item_category_id " +
                "    , ic.name AS category_name " +
                "FROM  " +
                "    Item i " +
                "LEFT JOIN  " +
                "        Item_category_mapping icm ON " +
                "            i.item_id = icm.item_id " +
                "LEFT JOIN " +
                "        Item_category ic ON " +
                "            icm.item_category_id = ic.item_category_id " +
                "WHERE " +
                "    i.name LIKE ?" +
                "    AND i.stock >= ? " +
                "ORDER BY " +
                "    i.item_id " +
                "    , icm.item_category_id;";

        try (PreparedStatement statement = MySqlConnectionManager.createPreparedStatement(query)) {
            int inStockCriteria = inStockOnly ? 1 : 0;
            statement.setString(1, "%" + searchTerm + "%");
            statement.setInt(2, inStockCriteria);
            boolean hasResult = statement.execute();
            if (!hasResult) throw new NoResultException("No items found");
            return getItemsFromResultSet(statement.getResultSet());
        }
        catch (SQLException e) {
            throw new QueryException("Failed to get items from database: " + e.getMessage());
        }
    }

    public static List<MySqlItem> getItemsByCategory(int categoryId, boolean inStockOnly) {
        if (categoryId <= 0) throw new IllegalArgumentException("categoryId cannot be <= 0");

        String query = 
                "SELECT " +
                "    i.item_id " +
                "    , i.name AS item_name " +
                "    , i.description " +
                "    , i.price " +
                "    , i.stock " +
                "    , icm.item_category_id " +
                "    , ic.name AS category_name " +
                "FROM " +
                "    Item i " +
                "LEFT JOIN " +
                "        Item_category_mapping icm ON " +
                "            i.item_id = icm.item_id " +
                "LEFT JOIN " +
                "        Item_category ic ON " +
                "            icm.item_category_id = ic.item_category_id " +
                "WHERE " +
                "    i.item_id IN ( " +
                "        SELECT DISTINCT " +
                "            icm.item_id " +
                "        FROM " +
                "            Item_category_mapping icm " +
                "        WHERE " +
                "            icm.item_category_id = ? " +
                "    ) " +
                "    AND i.stock >= ? " +
                "ORDER BY " +
                "    i.item_id " +
                "    , icm.item_category_id;";

        try (PreparedStatement statement = MySqlConnectionManager.createPreparedStatement(query)) {
            int inStockCriteria = inStockOnly ? 1 : 0;
            statement.setInt(1, categoryId);
            statement.setInt(2, inStockCriteria);
            boolean hasResult = statement.execute();
            if (!hasResult) throw new NoResultException("No items found");
            return getItemsFromResultSet(statement.getResultSet());
        }
        catch (SQLException e) {
            throw new QueryException("Failed to get items from database: " + e.getMessage());
        }

    }

    private static List<MySqlItem> getItemsFromResultSet(ResultSet resultSet) throws SQLException {
        ArrayList<Item> items = new ArrayList<>();
        int lastItemId = 0;
        int lastCategoryId = 0;
        while (resultSet.next()) {
            int itemId = resultSet.getInt("item_id");
            if (itemId != lastItemId) {
                String itemName = resultSet.getString("item_name");
                String description = resultSet.getString("description");
                float price = resultSet.getFloat("price");
                int stock = resultSet.getInt("stock");
                items.add(new Item(itemId, itemName, description, price, stock, null));
                lastItemId = itemId;
                lastCategoryId = 0;
            }

            int categoryId = resultSet.getInt("item_category_id");
            if (categoryId != lastCategoryId) {
                Item currentItem = items.get(items.size() - 1);
                String categoryName = resultSet.getString("category_name");
                currentItem.addCategory(new ItemCategory(categoryId, categoryName));
                lastCategoryId = categoryId;
            }
        }

        ArrayList<MySqlItem> resultList = new ArrayList<>();
        for (Item item : items) {
            resultList.add(new MySqlItem(
                    item.getId(),
                    item.getName(),
                    item.getDescription(),
                    item.getPrice(),
                    item.getStock(),
                    item.getCategories()));
        }
        return resultList;
    }

}
