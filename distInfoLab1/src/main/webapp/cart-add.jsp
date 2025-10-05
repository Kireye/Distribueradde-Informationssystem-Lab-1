<%@ include file="/WEB-INF/jspf/db-init.jspf" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.werkstrom.distinfolab1.ui.UserInfo" %>
<%@ page import="com.werkstrom.distinfolab1.bo.facades.UserFacade" %>
<%@ page import="com.werkstrom.distinfolab1.bo.facades.ItemFacade" %>
<%@ page import="com.werkstrom.distinfolab1.ui.ItemInfo" %>

<%
    // Endast POST – annars tillbaka till startsidan
    if (!"POST".equalsIgnoreCase(request.getMethod())) {
        response.sendRedirect(request.getContextPath() + "/index.jsp");
        return;
    }

    // 1) Måste vara inloggad för att lägga i varukorg
    UserInfo currentUser = null;
    if (session != null) {
        Object o = session.getAttribute("user");
        if (o instanceof UserInfo) {
            currentUser = (UserInfo) o;
        }
    }
    if (currentUser == null) {
        // Gäst -> skicka till login
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }

    // 2) Läs parametrar
    String productIdParam = request.getParameter("productId");
    String qtyParam = request.getParameter("qty");

    int productId = -1;
    int qty = 1;

    try {
        productId = Integer.parseInt(productIdParam);
    } catch (Exception ignored) { }

    try {
        qty = Integer.parseInt(qtyParam);
    } catch (Exception ignored) { }

    if (productId <= 0) {
        response.sendRedirect(request.getContextPath() + "/index.jsp");
        return;
    }
    if (qty <= 0) {
        qty = 1;
    }

    // 3) Hämta produktinfo (för lagerkontroll m.m.)
    ItemInfo item = null;
    try {
        item = ItemFacade.getItemById(productId);
    } catch (Exception e) {
        String err = java.net.URLEncoder.encode("Product not found", "UTF-8");
        response.sendRedirect(request.getContextPath() + "/item.jsp?id=" + productId + "&adderror=" + err);
        return;
    }

    // 4) Enkel lagerkontroll (klipp till max lager, eller fel om slut)
    if (item.getStock() <= 0) {
        String err = java.net.URLEncoder.encode("Item is out of stock", "UTF-8");
        response.sendRedirect(request.getContextPath() + "/item.jsp?id=" + productId + "&adderror=" + err);
        return;
    }
    if (qty > item.getStock()) {
        qty = item.getStock();
    }

    // 5) Lägg i varukorg via UserFacade
    //    Första försöket: INSERT (addToCart). Om raden redan finns -> fallback: addQuantityToCart (UPDATE).
    try {
        UserFacade.addToCart(currentUser.getId(), productId, qty);
    } catch (Exception ex) {
        // Troligen PK-konflikt (raden finns redan). Försök öka kvantiteten.
        try {
            UserFacade.addQuantityToCart(currentUser.getId(), productId, qty);
        } catch (Exception ex2) {
            String err = java.net.URLEncoder.encode("Failed to add to cart: " + ex2.getMessage(), "UTF-8");
            response.sendRedirect(request.getContextPath() + "/item.jsp?id=" + productId + "&adderror=" + err);
            return;
        }
    }

    // 6) Klart -> tillbaka till produktsidan med flagga
    response.sendRedirect(request.getContextPath() + "/item.jsp?id=" + productId + "&added=1");
%>