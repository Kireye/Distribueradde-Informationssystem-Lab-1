package com.werkstrom.distinfolab1.bo.facades;

import com.werkstrom.distinfolab1.bo.enums.UserRole;
import com.werkstrom.distinfolab1.ui.UserInfo;
import com.werkstrom.distinfolab1.db.MySqlConnectionManager;
import com.werkstrom.distinfolab1.db.MySqlUser;
import com.werkstrom.distinfolab1.db.exceptions.ConnectionException;
import com.werkstrom.distinfolab1.db.exceptions.QueryException;
import com.werkstrom.distinfolab1.db.exceptions.TransactionException;

public final class UserFacade {

    private UserFacade() { }

    public static UserInfo login(String email, String password)
            throws ConnectionException, QueryException, TransactionException {

        if (email == null) {
            throw new IllegalArgumentException("email cannot be null");
        }
        if (password == null) {
            throw new IllegalArgumentException("password cannot be null");
        }
        if (email.trim().isEmpty()) {
            throw new IllegalArgumentException("email cannot be empty");
        }
        if (password.trim().isEmpty()) {
            throw new IllegalArgumentException("password cannot be empty");
        }
        if (!MySqlConnectionManager.isConnected()) {
            throw new ConnectionException("No database connection. Initialize connection before calling login.");
        }

        // 1) Hämta domänanvändaren via DB-lagret
        MySqlUser user = MySqlUser.getUser(email, password);

        // 2) Växla DB-anslutning till rollens MySQL-konto (enkelt, globalt)
        MySqlConnectionManager.closeConnection();
        String role = user.getRole().getRoleName();
        MySqlConnectionManager.initializeConnection(role, role);

        System.out.println(role);

        // 3) Paketera till UI-objekt för vyn
        return new UserInfo(
                user.getId(),
                user.getRole(),
                user.getName(),
                user.getEmail(),
                user.getCart(),
                user.getOrders()
        );
    }

    public static UserInfo register(String name, String email, String password)
            throws ConnectionException, QueryException, TransactionException {

        if (name == null) throw new IllegalArgumentException("name cannot be null");
        if (email == null) throw new IllegalArgumentException("email cannot be null");
        if (password == null) throw new IllegalArgumentException("password cannot be null");
        if (name.trim().isEmpty()) throw new IllegalArgumentException("name cannot be empty");
        if (email.trim().isEmpty()) throw new IllegalArgumentException("email cannot be empty");
        if (password.trim().isEmpty()) throw new IllegalArgumentException("password cannot be empty");
        if (!MySqlConnectionManager.isConnected()) {
            throw new ConnectionException("No database connection. Initialize connection before calling register.");
        }

        // 1) Skapa användaren som CUSTOMER (görs på "guest"-anslutning, vilket du har GRANT för)
        MySqlUser.addUser(UserRole.CUSTOMER, name.trim(), email.trim(), password.trim());

        // 2) Hämta tillbaka domänanvändaren (fortfarande på guest)
        MySqlUser user = MySqlUser.getUser(email, password);

        // 3) Växla DB-anslutning till användarens roll (customer)
        MySqlConnectionManager.closeConnection();
        String role = user.getRole().getRoleName();
        MySqlConnectionManager.initializeConnection(role, role);

        // 4) Returnera UI-objektet (UserServlet lägger det i session)
        return new UserInfo(
                user.getId(),
                user.getRole(),
                user.getName(),
                user.getEmail(),
                user.getCart(),
                user.getOrders()
        );
    }

    public static void logout() throws ConnectionException {
        if (!MySqlConnectionManager.isConnected()) {
            throw new ConnectionException("No database connection. Initialize connection before calling logout.");
        }
        MySqlConnectionManager.closeConnection();
        MySqlConnectionManager.initializeConnection("guest", "guest");
        System.out.println("guest");
    }
}