package com.werkstrom.distinfolab1.db;

import com.werkstrom.distinfolab1.bo.enums.UserRole;
import org.junit.jupiter.api.Test;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class MySqlUserTest {

    @Test
    void getUser() {
        MySqlConnectionManager.initializeConnection("root", "MySQLRoot");
        MySqlUser user = MySqlUser.getUser("hermodr@asgard.se", "balder");
        System.out.println(user.toString());
    }

    @Test
    void updateUserRole() {
        MySqlConnectionManager.initializeConnection("root", "MySQLRoot");
        MySqlUser.updateUserRole(1, UserRole.CUSTOMER);
        try (PreparedStatement statement = MySqlConnectionManager.createPreparedStatement("SELECT * FROM User WHERE user_id = ?")) {
            statement.setInt(1, 1);
            ResultSet rs = statement.executeQuery();
            rs.next();
            String role = rs.getString("user_role");
            int userId = rs.getInt("user_id");
            String name = rs.getString("name");
            System.out.println(userId + " " + name + " " + role);
            assertEquals(1, userId);
            assertEquals("Hel", name);
            assertEquals(UserRole.CUSTOMER.getRoleName(), role);
            MySqlUser.updateUserRole(1, UserRole.ADMIN);
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}