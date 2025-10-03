package com.werkstrom.distinfolab1.db;

import com.werkstrom.distinfolab1.db.exceptions.ConnectionException;
import com.werkstrom.distinfolab1.db.exceptions.QueryException;

import java.sql.*;

public class MySqlConnectionManager {
    private static MySqlConnectionManager instance = null;
    private Connection connection = null;

    private MySqlConnectionManager(String username, String password) throws ConnectionException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/online_store", username, password);
        }
        catch (Exception e) {
            e.printStackTrace();
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
                throw new ConnectionException("Closing connection failed: " + e.getMessage());
            }
    }

    /* TODO: Delete?
    public static Connection getConnection() throws ConnectionException {
        if (!isConnected())
            throw new ConnectionException("No connection initialized.");
        return instance.connection;
    }
     */

    public static boolean isConnected() {
        if (instance == null || instance.connection == null) return false;
        try {
            return !instance.connection.isClosed();
        }
        catch (SQLException e) {
            throw new ConnectionException("Failure while getting connection status: " + e.getMessage());
        }
    }

    public static PreparedStatement createPreparedStatement(String query) throws QueryException {
        try {
            return instance.connection.prepareStatement(query);
        }
        catch (SQLException e) {
            throw new QueryException("Failure while trying to prepare statement" + e.getMessage());
        }
    }

    /* TODO: Delete?
    public static ResultSet executeQuery(PreparedStatement preparedStatement) throws QueryException {
        try {
            return preparedStatement.executeQuery();
        }
        catch (SQLException e) {
            throw new QueryException("Failure while trying to execute query" + e.getMessage());
        }
    }
     */
}
