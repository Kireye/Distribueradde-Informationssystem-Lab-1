package com.werkstrom.distinfolab1.db;

import com.werkstrom.distinfolab1.db.exceptions.ConnectionException;
import org.junit.jupiter.api.Test;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

class MySqlConnectionManagerTest {

    @Test
    void initializeConnection() {

    }

    @Test
    void closeConnection() {

    }

    @Test
    void isConnected() {

    }

    @Test
    void createPreparedStatement() {
        try {
            MySqlConnectionManager.initializeConnection("root", "MySQLRoot");
            PreparedStatement statement = MySqlConnectionManager.createPreparedStatement("SELECT * FROM User WHERE name = ? AND email = ? AND user_role = ?");
            statement.setString(1, "hel");
            statement.setString(2, "hel@helheim.se");
            statement.setString(3, "admin");
            ResultSet resultSet = statement.executeQuery();
            assertTrue(resultSet.next(), "Should be true as the result set should have 1 entry");
            String resultUserName = resultSet.getString("name");
            assertEquals("Hel", resultUserName, "These strings should both be \"Hel\"");
            assertFalse(resultSet.next(), "Should be false as the result set should not have a second entry");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        finally {
            MySqlConnectionManager.closeConnection();
        }
    }

    @Test
    void executeQuery() {

    }
}