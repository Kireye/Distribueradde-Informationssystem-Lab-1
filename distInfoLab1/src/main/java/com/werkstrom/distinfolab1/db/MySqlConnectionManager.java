package com.werkstrom.distinfolab1.db;

import com.werkstrom.distinfolab1.db.exceptions.ConnectionException;
import com.werkstrom.distinfolab1.db.exceptions.QueryException;
import com.werkstrom.distinfolab1.db.exceptions.TransactionException;

import java.sql.*;

public class MySqlConnectionManager {
    private static MySqlConnectionManager instance = null;
    private Connection connection = null;

    private MySqlConnectionManager(String username, String password) throws ConnectionException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/online_store?allowMultiQueries=true", username, password);
        }
        catch (Exception e) {
            throw new ConnectionException(e.getMessage());
        }

    }

    public static void initializeConnection(String username, String password) throws ConnectionException {
        if (isConnected())
            throw new ConnectionException("Connection already initialized. To open a new connection close the active connection first.");

        instance = new MySqlConnectionManager(username, password);
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

    public static boolean isConnected() {
        if (instance == null || instance.connection == null) return false;
        try {
            return !instance.connection.isClosed();
        }
        catch (SQLException e) {
            throw new ConnectionException("Failure while getting connection status: " + e.getMessage());
        }
    }

    public static PreparedStatement createPreparedStatement(String query) throws ConnectionException, QueryException {
        try {
            if (!isConnected())
                throw new ConnectionException("Cannot create prepared statement because the connection is closed.");
            return instance.connection.prepareStatement(query);
        }
        catch (SQLException e) {
            throw new QueryException("Failure while trying to prepare statement: " + e.getMessage());
        }
    }

    public static void startTransaction() throws ConnectionException {
        if (!isConnected())
            throw new ConnectionException("Cannot start transaction because the connection is closed.");
        try {
            instance.connection.setAutoCommit(false);
        }
        catch (SQLException e) {
            throw new ConnectionException("Failure while trying to start transaction: " + e.getMessage());
        }
    }

    public static void commitTransaction() throws TransactionException {
        if (!isConnected())
            throw new ConnectionException("Cannot commit transaction because the connection is closed.");
        try {
            if (instance.connection.getAutoCommit())
                throw new TransactionException("Cannot commit transaction because no transaction has been started.");
            instance.connection.commit();
            instance.connection.setAutoCommit(true);
        } catch (SQLException e) {
            throw new TransactionException("Failure while trying to commit transaction: " + e.getMessage());
        }
    }

    public static void rollbackTransaction() throws ConnectionException {
        if (!isConnected())
            throw new ConnectionException("Cannot rollback transaction because the connection is closed.");
        try {
            if (instance.connection.getAutoCommit())
                throw new TransactionException("Cannot rollback transaction because no transaction has been started.");
            instance.connection.rollback();
            instance.connection.setAutoCommit(true);
        } catch (SQLException e) {
            throw new TransactionException("Failure while trying to rollback transaction: " + e.getMessage());
        }
    }

}
