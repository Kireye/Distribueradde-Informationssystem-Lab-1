<%@ include file="/WEB-INF/jspf/db-init.jspf" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.werkstrom.distinfolab1.ui.UserInfo" %>
<%@ page import="com.werkstrom.distinfolab1.ui.ShoppingCartInfo" %>
<%@ page import="com.werkstrom.distinfolab1.ui.CartItemInfo" %>
<%@ page import="com.werkstrom.distinfolab1.ui.ItemInfo" %>
<%@ page import="com.werkstrom.distinfolab1.bo.facades.UserFacade" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Cart</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<%@ include file="/WEB-INF/jspf/header.jspf" %>

<%
  UserInfo u = null;
  if (session != null) {
      Object o = session.getAttribute("user");
      if (o instanceof UserInfo) u = (UserInfo) o;
  }
%>

<main class="container">
  <h1>Cart</h1>

<%
  if (u == null) {
%>
    <p>You are not logged in.</p>
    <p><a href="<%= request.getContextPath() %>/login.jsp">Go to login</a></p>
<%
  } else {
      ShoppingCartInfo cart = null;
      String loadErr = null;
      try {
          cart = UserFacade.getShoppingCart(u.getId());
      } catch (Exception e) {
          loadErr = e.getMessage();
      }

      if (loadErr != null) {
%>
        <p class="muted">Could not load your cart: <%= loadErr %></p>
<%
      } else if (cart == null || cart.getItems().isEmpty()) {
%>
        <p>Your cart is empty.</p>
<%
      } else {
%>
    <table class="table">
      <thead>
        <tr>
          <th>Product</th>
          <th>Price</th>
          <th class="right">Qty</th>
          <th class="right">Subtotal</th>
          <th></th>
        </tr>
      </thead>
      <tbody>
      <%
        for (CartItemInfo line : cart.getItems()) {
            ItemInfo it = line.getItem();
            int qty = line.getQuantity();
            float unit = it.getPrice();
            float sub = unit * qty;
      %>
        <tr>
          <td><a href="<%= request.getContextPath() %>/item.jsp?id=<%= it.getId() %>"><%= it.getName() %></a></td>
          <td><%= Math.round(unit) %> SEK</td>
          <td class="right"><%= qty %></td>
          <td class="right"><%= Math.round(sub) %> SEK</td>
          <td class="right">
            <form action="<%= request.getContextPath() %>/cart-update.jsp" method="post" style="display:inline;">
              <input type="hidden" name="op" value="dec">
              <input type="hidden" name="productId" value="<%= it.getId() %>">
              <button type="submit" class="btn-secondary">−</button>
            </form>
            <form action="<%= request.getContextPath() %>/cart-update.jsp" method="post" style="display:inline;">
              <input type="hidden" name="op" value="inc">
              <input type="hidden" name="productId" value="<%= it.getId() %>">
              <button type="submit">+</button>
            </form>
            <form action="<%= request.getContextPath() %>/cart-update.jsp" method="post" style="display:inline; margin-left:6px;">
              <input type="hidden" name="op" value="remove">
              <input type="hidden" name="productId" value="<%= it.getId() %>">
              <button type="submit" class="btn-secondary">Remove</button>
            </form>
          </td>
        </tr>
      <%
        }
      %>
      </tbody>
      <tfoot>
        <tr>
          <th colspan="3" class="right">Total</th>
          <th class="right"><%= Math.round(cart.getTotalPrice()) %> SEK</th>
          <th></th>
        </tr>
      </tfoot>
    </table>

    <div class="row gap" style="margin-top:12px;">
      <form action="<%= request.getContextPath() %>/cart-clear.jsp" method="post">
        <button type="submit" class="btn-secondary">Clear cart</button>
      </form>

      <a href="<%= request.getContextPath() %>/checkout.jsp">
        <button type="button">Checkout</button>
      </a>
    </div>
<%
      } // end else has items
  } // end else logged in
%>
</main>

</body>
</html>