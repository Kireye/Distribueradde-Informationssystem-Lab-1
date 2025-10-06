package com.werkstrom.distinfolab1.ui.servlets;

import com.werkstrom.distinfolab1.bo.facades.ItemFacade;
import com.werkstrom.distinfolab1.db.exceptions.ConnectionException;
import com.werkstrom.distinfolab1.db.exceptions.NoResultException;
import com.werkstrom.distinfolab1.db.exceptions.QueryException;
import com.werkstrom.distinfolab1.ui.ItemInfo;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "ItemServlet", urlPatterns = {"/items"})
public class ItemServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String q = req.getParameter("q");
        String cat = req.getParameter("catId");
        String inStock = req.getParameter("inStock");

        boolean inStockOnly = "1".equals(inStock) || "true".equalsIgnoreCase(inStock);
        Integer catId = null;
        try {
            if (cat != null && !cat.isBlank()) catId = Integer.parseInt(cat.trim());
        } catch (NumberFormatException ignored) { /* låt catId vara null */ }

        try {
            List<ItemInfo> items;
            if (q != null && !q.isBlank()) {
                items = ItemFacade.searchByName(q, inStockOnly);
            } else if (catId != null && catId > 0) {
                items = ItemFacade.listByCategory(catId, inStockOnly);
            } else {
                items = ItemFacade.listAll(inStockOnly);
            }

            // Bygg en *enkel* HTML-lista av kort. Inga helpers, bara rakt på.
            StringBuilder html = new StringBuilder();
            for (ItemInfo it : items) {
                String stockText = it.getStock() > 0 ? "I lager" : "Slut";

                html.append("<article class=\"card\">")
                        .append("<h3>").append(escape(it.getName())).append("</h3>")
                        .append("<p class=\"price\">").append(format(it.getPrice())).append(" kr</p>")
                        .append("<p class=\"muted\">").append(stockText).append("</p>")
                        .append("</article>");
            }
            if (items.isEmpty()) {
                html.append("<div class=\"card\" style=\"padding:16px;\">Inga produkter hittades.</div>");
            }

            req.setAttribute("itemsHtml", html.toString());
            req.getRequestDispatcher("/index.jsp").forward(req, resp);
        }
        catch (IllegalArgumentException e) {
            req.setAttribute("itemsHtml", "<div class=\"card\" style=\"padding:16px;\">Ogiltig förfrågan.</div>");
            req.setAttribute("error", e.getMessage());
            req.getRequestDispatcher("/index.jsp").forward(req, resp);
        }
        catch (NoResultException e) {
            req.setAttribute("itemsHtml", "<div class=\"card\" style=\"padding:16px;\">Inga produkter matchade din sökning.</div>");
            req.getRequestDispatcher("/index.jsp").forward(req, resp);
        }
        catch (ConnectionException | QueryException e) {
            req.setAttribute("itemsHtml", "<div class=\"card\" style=\"padding:16px;\">Kunde inte hämta produkter just nu.</div>");
            req.setAttribute("error", e.getMessage());
            req.getRequestDispatcher("/index.jsp").forward(req, resp);
        }
    }

    // Liten skyddsfunktion så inte item-namn kan bryta HTML:en.
    private static String escape(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }

    // Extremt enkel pris-formattering (2 decimaler, med punkt — funkar bra nog).
    private static String format(float price) {
        return String.format(java.util.Locale.ROOT, "%.2f", price).replace('.', ',');
    }
}
