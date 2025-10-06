package com.werkstrom.distinfolab1.ui.servlets;

import com.werkstrom.distinfolab1.bo.facades.OrderFacade;
import com.werkstrom.distinfolab1.db.exceptions.ConnectionException;
import com.werkstrom.distinfolab1.db.exceptions.NoResultException;
import com.werkstrom.distinfolab1.db.exceptions.QueryException;
import com.werkstrom.distinfolab1.ui.OrderInfo;
import com.werkstrom.distinfolab1.ui.OrderLineInfo;
import com.werkstrom.distinfolab1.ui.UserInfo;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

@WebServlet(name = "OrderServlet", urlPatterns = {"/orders", "/orders/*"})
public class OrderServlet extends HttpServlet {

    @Override protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserInfo user = requireUserOrForwardLogin(req, resp);
        if (user == null) return;

        String path = req.getPathInfo();
        if (path == null || "/".equals(path)) {
            // Lista alla ordrar
            try {
                List<OrderInfo> orders = OrderFacade.listOrders(user.getId());
                req.setAttribute("ordersHtml", buildOrdersListHtml(orders, req.getContextPath()));
            } catch (ConnectionException | QueryException e) {
                req.setAttribute("ordersHtml", "<div class=\"card\" style=\"padding:16px;\">Kunde inte hämta ordrar.</div>");
                req.setAttribute("error", e.getMessage());
            }
            req.getRequestDispatcher("/orders.jsp").forward(req, resp);
            return;
        }

        if ("/detail".equals(path)) {
            int id = parseInt(req.getParameter("id"));
            if (id <= 0) { resp.sendRedirect(req.getContextPath()+"/orders"); return; }
            try {
                OrderInfo order = OrderFacade.getOrder(user.getId(), id);
                req.setAttribute("orderHtml", buildOrderDetailHtml(order, req.getContextPath()));
            } catch (NoResultException e) {
                req.setAttribute("orderHtml", "<div class=\"card\" style=\"padding:16px;\">Ordern hittades inte.</div>");
            } catch (ConnectionException | QueryException e) {
                req.setAttribute("orderHtml", "<div class=\"card\" style=\"padding:16px;\">Fel vid hämtning av order.</div>");
                req.setAttribute("error", e.getMessage());
            }
            req.getRequestDispatcher("/order.jsp").forward(req, resp);
            return;
        }

        if ("/checkout".equals(path)) {
            // Visa checkout (vy-only)
            req.getRequestDispatcher("/checkout.jsp").forward(req, resp);
            return;
        }

        resp.sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    @Override protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserInfo user = requireUserOrForwardLogin(req, resp);
        if (user == null) return;

        String path = req.getPathInfo();
        if (path == null) path = "";

        if ("/place".equals(path)) {
            String payment = trimOrEmpty(req.getParameter("payment"));
            if (!"card".equals(payment) && !"invoice".equals(payment)) {
                req.setAttribute("error", "Välj betalningsmetod.");
                req.getRequestDispatcher("/checkout.jsp").forward(req, resp);
                return;
            }

            try {
                // Skapa order (tömmer vagnen på DB-sidan)
                OrderFacade.placeOrder(user.getId());

                // Redirect hem (index.jsp är welcome-file)
                resp.sendRedirect(req.getContextPath() + "/");
                return;
            } catch (ConnectionException | QueryException | IllegalArgumentException e) {
                req.setAttribute("error", e.getMessage());
                req.getRequestDispatcher("/checkout.jsp").forward(req, resp);
                return;
            }
        }

        resp.sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    /* ---------------- Hjälpare ---------------- */

    private UserInfo requireUserOrForwardLogin(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        Object o = (session == null) ? null : session.getAttribute("user");
        if (!(o instanceof UserInfo)) {
            req.setAttribute("error", "Du behöver vara inloggad för att fortsätta.");
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
            return null;
        }
        return (UserInfo) o;
    }

    private static int parseInt(String s) {
        try { return Integer.parseInt(s == null ? "0" : s.trim()); }
        catch (NumberFormatException e) { return 0; }
    }
    private static String trimOrEmpty(String s) { return s == null ? "" : s.trim(); }
    private static String escape(String s) {
        if (s == null) return "";
        return s.replace("&","&amp;").replace("<","&lt;").replace(">","&gt;");
    }
    private static String fmt(float v) { return String.format(Locale.ROOT, "%.2f", v).replace('.', ','); }
    private static String fmtDate(java.util.Date d) {
        if (d == null) return "";
        return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(d);
    }

    private static String buildOrdersListHtml(List<OrderInfo> orders, String ctx) {
        if (orders.isEmpty())
            return "<div class=\"card\" style=\"padding:16px;\">Du har inga ordrar ännu.</div>";

        StringBuilder sb = new StringBuilder();
        sb.append("<table class=\"table\">")
                .append("<thead><tr><th>Order</th><th>Status</th><th>Datum</th><th class=\"right\"></th><th></th></tr></thead><tbody>");

        for (OrderInfo o : orders) {
            sb.append("<tr>")
                    .append("<td>#").append(o.getOrderId()).append("</td>")
                    .append("<td>").append(escape(o.getStatus().name().toLowerCase())).append("</td>")
                    .append("<td>").append(fmtDate(o.getOrderDate())).append("</td>")
                    .append("<td class=\"right\"><a class=\"link\" href=\"").append(ctx)
                    .append("</tr>");
        }

        sb.append("</tbody></table>");
        return sb.toString();
    }

    private static String buildOrderDetailHtml(OrderInfo o, String ctx) {
        StringBuilder sb = new StringBuilder();
        sb.append("<div class=\"card\" style=\"padding:16px;\">")
                .append("<h2 style=\"margin-top:0;\">Order #").append(o.getOrderId()).append("</h2>")
                .append("<p class=\"muted\">Status: ").append(escape(o.getStatus().name().toLowerCase()))
                .append(" • Datum: ").append(fmtDate(o.getOrderDate())).append("</p>")
                .append("</div>");

        sb.append("<table class=\"table\">")
                .append("<thead><tr><th>Produkt</th><th class=\"right\">Pris</th><th class=\"right\">Antal</th><th class=\"right\">Delsumma</th></tr></thead>")
                .append("<tbody>");

        for (OrderLineInfo l : o.getLines()) {
            sb.append("<tr>")
                    .append("<td>").append(escape(l.getItem().getName())).append("</td>")
                    .append("<td class=\"right\">").append(fmt(l.getItem().getPrice())).append(" kr</td>")
                    .append("<td class=\"right\">").append(l.getQuantity()).append("</td>")
                    .append("<td class=\"right\">").append(fmt(l.getSubtotal())).append(" kr</td>")
                    .append("</tr>");
        }

        sb.append("</tbody>")
                .append("<tfoot><tr><th>Totalt</th><th></th><th></th><th class=\"right\">")
                .append(fmt(o.getTotal())).append(" kr</th></tr></tfoot>")
                .append("</table>");

        sb.append("<div class=\"right\" style=\"margin-top:12px;\">")
                .append("<a class=\"link\" href=\"").append(ctx).append("/orders\">Tillbaka till alla ordrar</a>")
                .append("</div>");

        return sb.toString();
    }
}