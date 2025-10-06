package com.werkstrom.distinfolab1.config;

import com.werkstrom.distinfolab1.db.MySqlConnectionManager;
import com.werkstrom.distinfolab1.db.exceptions.ConnectionException;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class AppInitListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            if (!MySqlConnectionManager.isConnected()) {
                MySqlConnectionManager.initializeConnection("guest", "guest");
                System.out.println("[AppInitListener] Database initialized as guest.");
            }
        } catch (ConnectionException e) {
            System.err.println("[AppInitListener] Failed to initialize DB: " + e.getMessage());
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        try {
            if (MySqlConnectionManager.isConnected()) {
                MySqlConnectionManager.closeConnection();
                System.out.println("[AppInitListener] Database connection closed on shutdown.");
            }
        } catch (Exception e) {
            System.err.println("[AppInitListener] Error closing DB connection: " + e.getMessage());
        }
    }
}