package com.werkstrom.distinfolab1.ui.servlets;

import com.werkstrom.distinfolab1.bo.facades.ItemFacade;
import com.werkstrom.distinfolab1.bo.facades.ItemCategoryFacade;
import com.werkstrom.distinfolab1.db.exceptions.ConnectionException;
import com.werkstrom.distinfolab1.db.exceptions.QueryException;
import com.werkstrom.distinfolab1.ui.ItemInfo;
import com.werkstrom.distinfolab1.ui.ItemCategoryInfo;

import jakarta.servlet.ServletContext;
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

        // 1) Säkra att kategorierna finns i applicationScope (cachas första gången)
        ensureCategoriesInAppScope(req);

        // 2) Läs filter-parametrar
        String q = req.getParameter("q");
        if (q != null) q = q.trim();

        String cat = req.getParameter("catId");
        String inStock = req.getParameter("inStock");

        boolean onlyInStock = "1".equals(inStock) || "true".equalsIgnoreCase(inStock);
        Integer categoryId = null;
        try {
            if (cat != null && !cat.isBlank()) categoryId = Integer.parseInt(cat.trim());
        } catch (NumberFormatException ignored) {}

        // 3) Hämta varor via fasaden och bygg enkel HTML
        try {
            List<ItemInfo> items = ItemFacade.search(q, categoryId, onlyInStock);

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
        catch (ConnectionException | QueryException e) {
            req.setAttribute("itemsHtml", "<div class=\"card\" style=\"padding:16px;\">Kunde inte hämta produkter just nu.</div>");
            req.setAttribute("error", e.getMessage());
            req.getRequestDispatcher("/index.jsp").forward(req, resp);
        }
    }

    /** Hämtar kategorier från DB via fasaden och cachar som färdig HTML i application-scope. */
    private void ensureCategoriesInAppScope(HttpServletRequest req) {
        ServletContext app = req.getServletContext();
        Object cached = app.getAttribute("categoriesHtml");
        if (cached instanceof String && !((String) cached).isEmpty()) return; // finns redan

        try {
            List<ItemCategoryInfo> cats = ItemCategoryFacade.getAllCategories();
            String html = buildCategoriesHtml(cats, req.getContextPath());
            app.setAttribute("categoriesHtml", html);
        } catch (Exception e) {
            // Fallback om DB inte nås just nu – visa statisk lista så headern funkar
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
        // Matchar seedade kategorier (1..7)
        return ""
                + "<a href=\"" + ctxPath + "/items?catId=1\">Electronics</a>"
                + "<a href=\"" + ctxPath + "/items?catId=2\">Home &amp; Kitchen</a>"
                + "<a href=\"" + ctxPath + "/items?catId=3\">Video Games &amp; Consoles</a>"
                + "<a href=\"" + ctxPath + "/items?catId=4\">Books</a>"
                + "<a href=\"" + ctxPath + "/items?catId=5\">Toys &amp; Games</a>"
                + "<a href=\"" + ctxPath + "/items?catId=6\">Sport &amp; Outdoor</a>"
                + "<a href=\"" + ctxPath + "/items?catId=7\">Tools</a>";
    }

    private static String escape(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }

    private static String format(float price) {
        return String.format(java.util.Locale.ROOT, "%.2f", price).replace('.', ',');
    }
}
