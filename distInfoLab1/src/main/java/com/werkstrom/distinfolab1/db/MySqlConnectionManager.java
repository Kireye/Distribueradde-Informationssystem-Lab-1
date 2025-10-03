package com.werkstrom.distinfolab1.db;

import com.werkstrom.distinfolab1.db.exceptions.ConnectionException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySqlConnectionManager {
    private static MySqlConnectionManager instance = null;
    private Connection connection = null;

    private MySqlConnectionManager(String username, String password) throws ConnectionException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/online_store", username, password);
        }
        catch (Exception e) {
            throw new ConnectionException(e.getMessage());
        }

    }

    public static Connection initializeConnection(String username, String password) throws ConnectionException {
        if (isConnected())
            throw new ConnectionException("Connection already initialized. To open a new connection close the active connection first.");

        instance = new MySqlConnectionManager(username, password);
        return instance.connection;
    }

    public static void closeConnection() {
        if (isConnected())
            try {
                instance.connection.close();
                instance = null;
            }
            catch (SQLException e) {
                throw new ConnectionException(e.getMessage());
            }
    }

    public static Connection getConnection() throws ConnectionException {
        if (!isConnected())
            throw new ConnectionException("No connection initialized.");
        return instance.connection;
    }

    public static boolean isConnected() {
        if (instance == null || instance.connection == null) return false;
        try {
            return !instance.connection.isClosed();
        }
        catch (SQLException e) {
            throw new ConnectionException(e.getMessage());
        }
    }
}
