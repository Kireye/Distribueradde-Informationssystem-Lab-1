package com.werkstrom.distinfolab1.db;

import org.junit.jupiter.api.Test;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class MySqlShoppingCartTest {
    @Test
    void addToCart() {
        MySqlConnectionManager.initializeConnection("root", "MySQLRoot");
        MySqlShoppingCart.addToCart(1, 1, 3);
        String query = "SELECT * FROM Shopping_cart WHERE user_id = 1 AND item_id = 1 AND quantity = 3;";
        try (PreparedStatement statement = MySqlConnectionManager.createPreparedStatement(query)) {
            statement.execute();
            ResultSet resultSet = statement.getResultSet();
            resultSet.next();
            int user_id = resultSet.getInt("user_id");
            int itemId = resultSet.getInt("item_id");
            int quantity = resultSet.getInt("quantity");
            assertEquals(1, user_id);
            assertEquals(1, itemId);
            assertEquals(3, quantity);
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
        finally {
            MySqlConnectionManager.closeConnection();
        }
    }

    @Test
    void addQuantityToCart() {
        MySqlConnectionManager.initializeConnection("root", "MySQLRoot");
        MySqlShoppingCart.addQuantityToCart(1, 1, 2);
        String query = "SELECT * FROM Shopping_cart WHERE user_id = 1 AND item_id = 1 AND quantity = 5;";
        try (PreparedStatement statement = MySqlConnectionManager.createPreparedStatement(query)) {
            statement.execute();
            ResultSet resultSet = statement.getResultSet();
            resultSet.next();
            int user_id = resultSet.getInt("user_id");
            int itemId = resultSet.getInt("item_id");
            int quantity = resultSet.getInt("quantity");
            assertEquals(1, user_id);
            assertEquals(1, itemId);
            assertEquals(5, quantity);
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
        finally {
            MySqlConnectionManager.closeConnection();
        }
    }

    @Test
    void removeQuantityFromCart() {
        MySqlConnectionManager.initializeConnection("root", "MySQLRoot");
        MySqlShoppingCart.removeQuantityFromCart(1, 1, 4);
        String query = "SELECT * FROM Shopping_cart WHERE user_id = 1 AND item_id = 1 AND quantity = 1;";
        try (PreparedStatement statement = MySqlConnectionManager.createPreparedStatement(query)) {
            statement.execute();
            ResultSet resultSet = statement.getResultSet();
            resultSet.next();
            int user_id = resultSet.getInt("user_id");
            int itemId = resultSet.getInt("item_id");
            int quantity = resultSet.getInt("quantity");
            assertEquals(1, user_id);
            assertEquals(1, itemId);
            assertEquals(1, quantity);
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
        finally {
            MySqlConnectionManager.closeConnection();
        }
    }

    @Test
    void removeFromCart() {
        MySqlConnectionManager.initializeConnection("root", "MySQLRoot");
        MySqlShoppingCart.removeFromCart(1, 1);
        String query = "SELECT * FROM Shopping_cart WHERE user_id = 1 AND item_id = 1;";
        try (PreparedStatement statement = MySqlConnectionManager.createPreparedStatement(query)) {
            statement.execute();
            ResultSet resultSet = statement.getResultSet();
            assertFalse(resultSet.next());
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
        finally {
            MySqlConnectionManager.closeConnection();
        }
    }

    @Test
    void emptyCart() {
        MySqlConnectionManager.initializeConnection("root", "MySQLRoot");
        String query = "INSERT INTO Shopping_cart (user_id, item_id, quantity) VALUES (1, 1, 3);";
        try (PreparedStatement statement = MySqlConnectionManager.createPreparedStatement(query)) {
            statement.execute();
        }
        catch (SQLException e) {
            MySqlConnectionManager.closeConnection();
            throw new RuntimeException(e);
        }

        query = "INSERT INTO Shopping_cart (user_id, item_id, quantity) VALUES (1, 2, 5);";
        try (PreparedStatement statement = MySqlConnectionManager.createPreparedStatement(query)) {
            statement.execute();
        }
        catch (SQLException e) {
            MySqlConnectionManager.closeConnection();
            throw new RuntimeException(e);
        }

        MySqlShoppingCart.emptyCart(1);
        query = "SELECT * FROM Shopping_cart WHERE user_id = 1;";
        try (PreparedStatement statement = MySqlConnectionManager.createPreparedStatement(query)) {
            statement.execute();
            ResultSet resultSet = statement.getResultSet();
            assertFalse(resultSet.next());
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
        finally {
            MySqlConnectionManager.closeConnection();
        }
    }

    @Test
    void getShoppingCart() {
        MySqlConnectionManager.initializeConnection("root", "MySQLRoot");
        MySqlShoppingCart cart = MySqlShoppingCart.getShoppingCart(3);
        assertNotNull(cart);
        System.out.println(cart);
    }
}