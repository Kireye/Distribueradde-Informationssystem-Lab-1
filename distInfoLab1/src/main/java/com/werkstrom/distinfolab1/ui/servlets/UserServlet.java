package com.werkstrom.distinfolab1.ui.servlets;

import com.werkstrom.distinfolab1.bo.facades.UserFacade;
import com.werkstrom.distinfolab1.ui.UserInfo;
import com.werkstrom.distinfolab1.db.exceptions.ConnectionException;
import com.werkstrom.distinfolab1.db.exceptions.QueryException;
import com.werkstrom.distinfolab1.db.exceptions.TransactionException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet(name = "UserServlet", urlPatterns = {"/user/*"})
public class UserServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String path = req.getPathInfo();
        if (path == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        if ("/login".equals(path)) {
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
            return;
        }

        if ("/logout".equals(path)) {
            // Visa en enkel bekräftelse- eller redirect direkt
            resp.sendRedirect(req.getContextPath() + "/index.jsp");
            return;
        }

        resp.sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String path = req.getPathInfo();

        if (path == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        if ("/login".equals(path)) {
            handleLogin(req, resp);
            return;
        }

        if ("/logout".equals(path)) {
            handleLogout(req, resp);
            return;
        }

        resp.sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    private void handleLogin(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String email = req.getParameter("email");
        String password = req.getParameter("password");

        if (email == null) {
            email = "";
        }
        if (password == null) {
            password = "";
        }

        if (email.trim().isEmpty() || password.trim().isEmpty()) {
            req.setAttribute("error", "Fyll i både e-post och lösenord.");
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
            return;
        }

        try {
            UserInfo userInfo = UserFacade.login(email, password);

            HttpSession session = req.getSession(true);
            session.setAttribute("user", userInfo);

            resp.sendRedirect(req.getContextPath() + "/index.jsp");
        }
        catch (IllegalArgumentException e) {
            req.setAttribute("error", e.getMessage());
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
        }
        catch (ConnectionException | QueryException | TransactionException e) {
            req.setAttribute("error", "Inloggning misslyckades: " + e.getMessage());
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
        }
    }

    private void handleLogout(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {
        try {
            UserFacade.logout();
        } catch (ConnectionException e) {
            req.setAttribute("error", "Kunde inte byta till gästanvändare: " + e.getMessage());
        }

        HttpSession session = req.getSession(false);
        if (session != null) {
            session.removeAttribute("user");
            session.invalidate();
        }

        resp.sendRedirect(req.getContextPath() + "/index.jsp");
    }
}