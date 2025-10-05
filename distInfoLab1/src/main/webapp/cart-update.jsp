<%@ include file="/WEB-INF/jspf/db-init.jspf" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.werkstrom.distinfolab1.ui.UserInfo" %>
<%@ page import="com.werkstrom.distinfolab1.bo.facades.UserFacade" %>
<%@ page import="com.werkstrom.distinfolab1.bo.facades.ItemFacade" %>
<%@ page import="com.werkstrom.distinfolab1.ui.ShoppingCartInfo" %>
<%@ page import="com.werkstrom.distinfolab1.ui.CartItemInfo" %>
<%@ page import="com.werkstrom.distinfolab1.ui.ItemInfo" %>
<%@ page import="java.net.URLEncoder" %>
<%
  if (!"POST".equalsIgnoreCase(request.getMethod())) {
      response.sendRedirect(request.getContextPath() + "/cart.jsp");
      return;
  }

  UserInfo u = (session != null && session.getAttribute("user") instanceof UserInfo)
          ? (UserInfo) session.getAttribute("user") : null;

  if (u == null) {
      // Gäst -> tvinga login innan korg-manipulation
      response.sendRedirect(request.getContextPath() + "/login.jsp");
      return;
  }

  String op = request.getParameter("op"); // add | inc | dec | remove | clear
  String pid = request.getParameter("productId");
  String qtyParam = request.getParameter("qty");

  int itemId = -1;
  int qty = 1;
  try { if (pid != null) itemId = Integer.parseInt(pid); } catch (Exception ignored) {}
  try { if (qtyParam != null) qty = Integer.parseInt(qtyParam); } catch (Exception ignored) {}

  try {
      if ("add".equalsIgnoreCase(op)) {
          if (itemId <= 0) {
              response.sendRedirect(request.getContextPath() + "/index.jsp");
              return;
          }
          if (qty <= 0) qty = 1;

          // Lagerkontroll via ItemFacade
          ItemInfo item = ItemFacade.getItemById(itemId);
          if (item.getStock() <= 0) {
              String err = URLEncoder.encode("Item is out of stock", "UTF-8");
              response.sendRedirect(request.getContextPath() + "/item.jsp?id=" + itemId + "&adderror=" + err);
              return;
          }
          if (qty > item.getStock()) qty = item.getStock();

          // Kolla om raden finns i korgen → välj rätt operation
          ShoppingCartInfo cartNow = UserFacade.getShoppingCart(u.getId());
          boolean alreadyInCart = false;
          for (CartItemInfo line : cartNow.getItems()) {
              if (line.getItem().getId() == itemId) {
                  alreadyInCart = true;
                  break;
              }
          }

          try {
              if (alreadyInCart) {
                  UserFacade.addQuantityToCart(u.getId(), itemId, qty); // UPDATE
              } else {
                  UserFacade.addToCart(u.getId(), itemId, qty);         // INSERT
              }
              response.sendRedirect(request.getContextPath() + "/item.jsp?id=" + itemId + "&added=1");
              return;
          } catch (Exception ex) {
              String err = URLEncoder.encode("Cart operation failed: " + ex.getMessage(), "UTF-8");
              response.sendRedirect(request.getContextPath() + "/item.jsp?id=" + itemId + "&adderror=" + err);
              return;
          }
      }


      if ("clear".equalsIgnoreCase(op)) {
          UserFacade.emptyCart(u.getId());
          response.sendRedirect(request.getContextPath() + "/cart.jsp");
          return;
      }

      // För inc/dec/remove behöver vi aktuell mängd för att undvika quantity=0 (CHECK-fel)
      if (itemId <= 0) {
          response.sendRedirect(request.getContextPath() + "/cart.jsp");
          return;
      }

      ShoppingCartInfo cart = UserFacade.getShoppingCart(u.getId());
      int currentQty = 0;
      for (CartItemInfo line : cart.getItems()) {
          if (line.getItem().getId() == itemId) {
              currentQty = line.getQuantity();
              break;
          }
      }

      if ("inc".equalsIgnoreCase(op)) {
          UserFacade.addQuantityToCart(u.getId(), itemId, 1);
      } else if ("dec".equalsIgnoreCase(op)) {
          if (currentQty <= 1) {
              UserFacade.removeFromCart(u.getId(), itemId); // radera raden istället för 0
          } else {
              UserFacade.removeQuantityFromCart(u.getId(), itemId, 1);
          }
      } else if ("remove".equalsIgnoreCase(op)) {
          UserFacade.removeFromCart(u.getId(), itemId);
      }

      response.sendRedirect(request.getContextPath() + "/cart.jsp");
      return;

  } catch (Exception e) {
      String err = URLEncoder.encode("Cart operation failed: " + e.getMessage(), "UTF-8");
      // Skicka tillbaka antingen till cart.jsp eller item.jsp beroende på op
      if ("add".equalsIgnoreCase(op)) {
          response.sendRedirect(request.getContextPath() + "/item.jsp?id=" + itemId + "&adderror=" + err);
      } else {
          response.sendRedirect(request.getContextPath() + "/cart.jsp?error=" + err);
      }
      return;
  }
%>