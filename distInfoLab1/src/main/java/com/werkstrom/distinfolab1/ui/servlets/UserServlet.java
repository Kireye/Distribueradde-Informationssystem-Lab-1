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
            resp.sendRedirect(req.getContextPath() + "/items");
            return;
        }

        if ("/login".equals(path)) {
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
            return;
        }

        // I doGet:
        if ("/register".equals(path)) {
            req.getRequestDispatcher("/register.jsp").forward(req, resp);
            return;
        }

        if ("/logout".equals(path)) {
            // GET /user/logout -> tillbaka till produktlistan
            resp.sendRedirect(req.getContextPath() + "/items");
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

        // I doPost:
        if ("/register".equals(path)) {
            handleRegister(req, resp);
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

        if (email == null) email = "";
        if (password == null) password = "";

        if (email.trim().isEmpty() || password.trim().isEmpty()) {
            req.setAttribute("error", "Fyll i både e-post och lösenord.");
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
            return;
        }

        try {
            UserInfo userInfo = UserFacade.login(email, password);

            HttpSession session = req.getSession(true);
            session.setAttribute("user", userInfo);

            // Efter lyckad login: gå till ItemServlet
            resp.sendRedirect(req.getContextPath() + "/items");
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

    private void handleRegister(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {
        String name = req.getParameter("name");
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        if (name == null) name = "";
        if (email == null) email = "";
        if (password == null) password = "";

        if (name.trim().isEmpty() || email.trim().isEmpty() || password.trim().isEmpty()) {
            req.setAttribute("error", "Fyll i namn, e-post och lösenord.");
            req.getRequestDispatcher("/register.jsp").forward(req, resp);
            return;
        }

        try {
            // Skapar kundkonto via Facade (se punkt 3)
            com.werkstrom.distinfolab1.bo.facades.UserFacade.register(name, email, password);

            // Klart – redirecta hem (eller byt till "/login.jsp" om du hellre vill)
            resp.sendRedirect(req.getContextPath() + "/");
        } catch (Exception e) {
            req.setAttribute("error", "Kunde inte skapa konto: " + e.getMessage());
            req.getRequestDispatcher("/register.jsp").forward(req, resp);
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

        // Efter logout: gå till ItemServlet
        resp.sendRedirect(req.getContextPath() + "/items");
    }
}