package com.werkstrom.distinfolab1.ui.servlets;

import com.werkstrom.distinfolab1.bo.facades.ItemCategoryFacade;
import com.werkstrom.distinfolab1.bo.facades.ItemFacade;
import com.werkstrom.distinfolab1.db.exceptions.ConnectionException;
import com.werkstrom.distinfolab1.db.exceptions.QueryException;
import com.werkstrom.distinfolab1.ui.ItemCategoryInfo;
import com.werkstrom.distinfolab1.ui.ItemInfo;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@WebServlet(name = "ItemServlet", urlPatterns = {"/items", "/items/*"})
public class ItemServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        ensureCategoriesInAppScope(req); // cachar kategorilänkar i applicationScope

        String path = req.getPathInfo(); // null för /items, "/detail" för /items/detail
        if (path != null && path.equals("/detail")) {
            handleDetail(req, resp);
            return;
        }
        handleList(req, resp);
    }

    /* ---------- LISTA ---------- */
    private void handleList(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String q = trimOrNull(req.getParameter("q"));
        String cat = trimOrNull(req.getParameter("catId"));
        String inStock = trimOrNull(req.getParameter("inStock"));

        boolean onlyInStock = "1".equals(inStock) || "true".equalsIgnoreCase(inStock);
        Integer categoryId = null;
        try { if (cat != null) categoryId = Integer.parseInt(cat); } catch (NumberFormatException ignored) {}

        try {
            List<ItemInfo> items = ItemFacade.search(q, categoryId, onlyInStock);

            String ctx = req.getContextPath();
            StringBuilder html = new StringBuilder();
            for (ItemInfo it : items) {
                String stockText = it.getStock() > 5 ? "I lager"
                        : it.getStock() > 0 ? "Få kvar"
                        : "Slut";
                html.append("<a class=\"card\" href=\"")
                        .append(ctx).append("/items/detail?id=").append(it.getId()).append("\">")
                        .append("<h3>").append(escape(it.getName())).append("</h3>")
                        .append("<p class=\"price\">").append(format(it.getPrice())).append(" kr</p>")
                        .append("<p class=\"muted\">").append(stockText).append("</p>")
                        .append("</a>");
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
        catch (ConnectionException | QueryException e) {
            req.setAttribute("itemsHtml", "<div class=\"card\" style=\"padding:16px;\">Kunde inte hämta produkter just nu.</div>");
            req.setAttribute("error", e.getMessage());
            req.getRequestDispatcher("/index.jsp").forward(req, resp);
        }
    }

    /* ---------- DETALJ ---------- */
    private void handleDetail(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String idParam = trimOrNull(req.getParameter("id"));
        int id;
        try {
            id = Integer.parseInt(idParam);
        } catch (Exception e) {
            resp.sendRedirect(req.getContextPath() + "/items");
            return;
        }

        try {
            ItemInfo it = ItemFacade.getItemById(id);

            // Förbered vy-attribut
            req.setAttribute("item", it);
            req.setAttribute("price", format(it.getPrice()));

            String stockClass, stockText;
            if (it.getStock() > 5) {
                stockClass = "in-stock";  stockText = "I lager";
            }
            else if (it.getStock() > 0) {
                stockClass = "low-stock"; stockText = "Få kvar";
            }
            else {
                stockClass = "out-stock"; stockText = "Slut";
            }
            req.setAttribute("stockClass", stockClass);
            req.setAttribute("stockText", stockText);

            String categoryLine = it.getCategories().stream()
                    .map(c -> c.getName())
                    .collect(Collectors.joining(", "));
            req.setAttribute("categoryLine", categoryLine);

            // Bild – placeholder tills du har riktig bildhantering
            String imageUrl = req.getContextPath() + "/images/item-" + it.getId() + ".jpg";
            req.setAttribute("imageUrl", imageUrl);

            int qtyMax = Math.max(0, it.getStock());
            int qtyDefault = it.getStock() > 0 ? 1 : 0;
            String qtyInputAttrs = it.getStock() > 0 ? "" : "disabled";
            String addBtnAttrs = it.getStock() > 0 ? "" : "class=\"btn-disabled\" disabled";
            req.setAttribute("qtyMax", qtyMax);
            req.setAttribute("qtyDefault", qtyDefault);
            req.setAttribute("qtyInputAttrs", qtyInputAttrs);
            req.setAttribute("addBtnAttrs", addBtnAttrs);

            req.getRequestDispatcher("/item.jsp").forward(req, resp);
        }
        catch (ConnectionException | QueryException e) {
            req.setAttribute("itemsHtml", "<div class=\"card\" style=\"padding:16px;\">Kunde inte hämta produkten.</div>");
            req.setAttribute("error", e.getMessage());
            req.getRequestDispatcher("/index.jsp").forward(req, resp);
        }
        catch (IllegalArgumentException e) {
            resp.sendRedirect(req.getContextPath() + "/items");
        }
    }

    /* ---------- Kategorier i application-scope ---------- */
    private void ensureCategoriesInAppScope(HttpServletRequest req) {
        ServletContext app = req.getServletContext();
        Object cached = app.getAttribute("categoriesHtml");
        if (cached instanceof String && !((String) cached).isEmpty()) return;

        try {
            List<ItemCategoryInfo> cats = ItemCategoryFacade.getAllCategories();
            String html = buildCategoriesHtml(cats, req.getContextPath());
            app.setAttribute("categoriesHtml", html);
        } catch (Exception e) {
            app.setAttribute("categoriesHtml", getStaticFallbackCategoriesHtml(req.getContextPath()));
        }
    }

    private static String buildCategoriesHtml(List<ItemCategoryInfo> cats, String ctxPath) {
        StringBuilder sb = new StringBuilder();
        for (ItemCategoryInfo c : cats) {
            sb.append("<a href=\"").append(ctxPath).append("/items?catId=").append(c.getId()).append("\">")
                    .append(escape(c.getName()))
                    .append("</a>");
        }
        return sb.toString();
    }

    private static String getStaticFallbackCategoriesHtml(String ctxPath) {
        return ""
                + "<a href=\"" + ctxPath + "/items?catId=1\">Electronics</a>"
                + "<a href=\"" + ctxPath + "/items?catId=2\">Home &amp; Kitchen</a>"
                + "<a href=\"" + ctxPath + "/items?catId=3\">Video Games &amp; Consoles</a>"
                + "<a href=\"" + ctxPath + "/items?catId=4\">Books</a>"
                + "<a href=\"" + ctxPath + "/items?catId=5\">Toys &amp; Games</a>"
                + "<a href=\"" + ctxPath + "/items?catId=6\">Sport &amp; Outdoor</a>"
                + "<a href=\"" + ctxPath + "/items?catId=7\">Tools</a>";
    }

    /* ---------- Hjälpare ---------- */
    private static String trimOrNull(String s) {
        if (s == null) return null;
        String t = s.trim();
        return t.isEmpty() ? null : t;
    }

    private static String escape(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }

    private static String format(float price) {
        return String.format(Locale.ROOT, "%.2f", price).replace('.', ',');
    }
}