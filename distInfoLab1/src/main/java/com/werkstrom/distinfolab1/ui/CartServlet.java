package com.werkstrom.distinfolab1.ui;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "cartServlet", value = "/cart")
public class CartServlet extends HttpServlet {

    public static class CartItem {
        public String productId;
        public String name;
        public int price; // kr för enkelhet
        public int qty;
    }

    @SuppressWarnings("unchecked")
    private List<CartItem> getCart(HttpSession session) {
        Object obj = session.getAttribute("cart");
        if (obj == null) {
            List<CartItem> cart = new ArrayList<>();
            session.setAttribute("cart", cart);
            return cart;
        }
        return (List<CartItem>) obj;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {

        HttpSession session = req.getSession(true);
        List<CartItem> cart = getCart(session);

        String productId = req.getParameter("productId");
        String name = req.getParameter("productName");
        String priceStr = req.getParameter("price");
        String qtyStr = req.getParameter("qty");

        int price = 0;
        int qty = 1;

        if (priceStr != null) {
            try {
                price = Integer.parseInt(priceStr);
            } catch (NumberFormatException ignored) { }
        }
        if (qtyStr != null) {
            try {
                qty = Integer.parseInt(qtyStr);
            } catch (NumberFormatException ignored) { }
        }

        boolean found = false;
        for (CartItem item : cart) {
            if (item.productId != null && item.productId.equals(productId)) {
                item.qty = item.qty + qty;
                found = true;
            }
        }

        if (!found) {
            CartItem item = new CartItem();
            item.productId = productId;
            item.name = name;
            item.price = price;
            item.qty = qty;
            cart.add(item);
        }

        resp.sendRedirect(req.getContextPath() + "/index.jsp");
    }
}
