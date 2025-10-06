package com.werkstrom.distinfolab1.db;

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

}