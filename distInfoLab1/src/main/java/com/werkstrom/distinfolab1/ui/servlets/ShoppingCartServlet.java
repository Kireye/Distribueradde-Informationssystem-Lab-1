package com.werkstrom.distinfolab1.ui.servlets;

import com.werkstrom.distinfolab1.bo.facades.ShoppingCartFacade;
import com.werkstrom.distinfolab1.db.exceptions.ConnectionException;
import com.werkstrom.distinfolab1.db.exceptions.QueryException;
import com.werkstrom.distinfolab1.ui.CartItemInfo;
import com.werkstrom.distinfolab1.ui.ShoppingCartInfo;
import com.werkstrom.distinfolab1.ui.UserInfo;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Locale;

@WebServlet(name = "ShoppingCartServlet", urlPatterns = {"/cart", "/cart/*"})
public class ShoppingCartServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        UserInfo user = requireUserOrForwardLogin(req, resp);
        if (user == null) return;

        try {
            ShoppingCartInfo cart = ShoppingCartFacade.getCart(user.getId());
            req.setAttribute("cartHtml", buildCartHtml(cart, req.getContextPath()));
            req.getRequestDispatcher("/cart.jsp").forward(req, resp);
        } catch (ConnectionException | QueryException e) {
            req.setAttribute("cartHtml", "<div class=\"card\" style=\"padding:16px;\">Kunde inte hämta din varukorg.</div>");
            req.setAttribute("error", e.getMessage());
            req.getRequestDispatcher("/cart.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String path = req.getPathInfo();
        if (path == null) path = "";

        UserInfo user = requireUserOrForwardLogin(req, resp);
        if (user == null) return;

        try {
            switch (path) {
                case "/add": {
                    int itemId = parseInt(req.getParameter("itemId"));
                    int qty = Math.max(1, parseInt(req.getParameter("qty")));
                    ShoppingCartFacade.add(user.getId(), itemId, qty);
                    break;
                }
                case "/update": {
                    int itemId = parseInt(req.getParameter("itemId"));
                    int qty = Math.max(0, parseInt(req.getParameter("qty")));
                    ShoppingCartFacade.updateQuantity(user.getId(), itemId, qty);
                    break;
                }
                case "/remove": {
                    int itemId = parseInt(req.getParameter("itemId"));
                    ShoppingCartFacade.remove(user.getId(), itemId);
                    break;
                }
                case "/clear": {
                    ShoppingCartFacade.clear(user.getId());
                    break;
                }
                default:
                    // Om okänd POST, visa korgen
                    break;
            }
        } catch (ConnectionException | QueryException | IllegalArgumentException e) {
            req.setAttribute("error", e.getMessage());
        }

        // Tillbaka till korgen
        resp.sendRedirect(req.getContextPath() + "/cart");
    }

    /* ---------------- Hjälpare ---------------- */

    private UserInfo requireUserOrForwardLogin(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        Object o = (session == null) ? null : session.getAttribute("user");
        if (!(o instanceof UserInfo)) {
            req.setAttribute("error", "Du behöver vara inloggad för att använda varukorgen.");
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
            return null;
        }
        return (UserInfo) o;
    }

    private static int parseInt(String s) {
        try {
            return Integer.parseInt(s == null ? "0" : s.trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private static String buildCartHtml(ShoppingCartInfo cart, String ctx) {
        if (cart.getItems().isEmpty()) {
            return "<div class=\"card\" style=\"padding:16px;\">Din varukorg är tom.</div>";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("<table class=\"table\">")
                .append("<thead><tr>")
                .append("<th>Produkt</th><th class=\"right\">Pris</th><th class=\"right\">Antal</th><th class=\"right\">Delsumma</th><th></th>")
                .append("</tr></thead><tbody>");

        for (CartItemInfo ci : cart.getItems()) {
            sb.append("<tr>")
                    .append("<td>").append(escape(ci.getName())).append("</td>")
                    .append("<td class=\"right\">").append(fmt(ci.getPrice())).append(" kr</td>")
                    .append("<td class=\"right\">")
                    .append("<form action=\"").append(ctx).append("/cart/update\" method=\"post\" class=\"row\" style=\"justify-content:flex-end\">")
                    .append("<input type=\"hidden\" name=\"itemId\" value=\"").append(ci.getItemId()).append("\">")
                    .append("<input type=\"number\" name=\"qty\" min=\"0\" max=\"").append(Math.max(0, ci.getStock())).append("\" value=\"").append(ci.getQuantity()).append("\" style=\"width:80px\">")
                    .append("<button type=\"submit\" class=\"btn-secondary\">Uppdatera</button>")
                    .append("</form>")
                    .append("</td>")
                    .append("<td class=\"right\">").append(fmt(ci.getSubtotal())).append(" kr</td>")
                    .append("<td class=\"right\">")
                    .append("<form action=\"").append(ctx).append("/cart/remove\" method=\"post\">")
                    .append("<input type=\"hidden\" name=\"itemId\" value=\"").append(ci.getItemId()).append("\">")
                    .append("<button type=\"submit\" class=\"btn-secondary\">Ta bort</button>")
                    .append("</form>")
                    .append("</td>")
                    .append("</tr>");
        }

        sb.append("</tbody>")
                .append("<tfoot><tr>")
                .append("<th>Totalt</th><th></th><th class=\"right\">").append(cart.getTotalQuantity()).append("</th>")
                .append("<th class=\"right\">").append(fmt(cart.getTotal())).append(" kr</th>")
                .append("<th class=\"right\">")
                .append("<form action=\"").append(ctx).append("/cart/clear\" method=\"post\">")
                .append("<button type=\"submit\" class=\"btn-secondary\">Töm varukorg</button>")
                .append("</form>")
                .append("</th>")
                .append("</tr></tfoot>")
                .append("</table>");

        return sb.toString();
    }

    private static String escape(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }

    private static String fmt(float price) {
        return String.format(Locale.ROOT, "%.2f", price).replace('.', ',');
    }
}
