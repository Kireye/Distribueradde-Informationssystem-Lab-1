<%@ include file="/WEB-INF/jspf/db-init.jspf" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.werkstrom.distinfolab1.bo.facades.ItemFacade" %>
<%@ page import="com.werkstrom.distinfolab1.ui.ItemInfo" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Product</title>
  <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css?v=11">
</head>
<body>

<%@ include file="/WEB-INF/jspf/header.jspf" %>

<%
  String idParam = request.getParameter("id");
  ItemInfo item = null;
  String err = null;

  if (idParam == null) {
      err = "No product id specified.";
  } else {
      try {
          int itemId = Integer.parseInt(idParam);
          item = ItemFacade.getItemById(itemId);
      } catch (NumberFormatException nfe) {
          err = "Invalid product id.";
      } catch (Exception e) {
          err = "Could not fetch product: " + e.getMessage();
      }
  }
%>

<main class="container product-page">
<%
  if (err != null) {
%>
    <section class="card">
      <h2>Error</h2>
      <p><%= err %></p>
      <p><a href="<%= request.getContextPath() %>/index.jsp">Back to products</a></p>
    </section>
<%
  } else if (item == null) {
%>
    <section class="card">
      <p>Product not found.</p>
      <p><a href="<%= request.getContextPath() %>/index.jsp">Back to products</a></p>
    </section>
<%
  } else {
      String name = item.getName();
      String description = item.getDescription();
      float price = item.getPrice();
      int stock = item.getStock();

      String stockText = "Out of stock";
      String stockClass = "out-stock";
      if (stock > 10) {
          stockText = "In stock";
          stockClass = "in-stock";
      } else if (stock > 0) {
          stockText = "Low stock";
          stockClass = "low-stock";
      }

      int maxSelectable = stock;
      if (maxSelectable > 10) {
          maxSelectable = 10;
      }
%>

  <section class="gallery">
    <img src="<%= request.getContextPath() %>/images/placeholder.jpg" alt="<%= name %>" class="main-image">
  </section>

  <section class="details">
    <h1 class="product-title"><%= name %></h1>

    <div class="meta">
      <div><span class="muted">SKU:</span> <%= item.getId() %></div>
      <div><span class="muted">Categories:</span>
        <%
          java.util.List<com.werkstrom.distinfolab1.bo.ItemCategory> cats = item.getCategories();
          if (cats == null || cats.isEmpty()) {
        %>
            <span>—</span>
        <%
          } else {
              for (int i = 0; i < cats.size(); i++) {
                  out.print(cats.get(i).getName());
                  if (i < cats.size() - 1) {
                      out.print(", ");
                  }
              }
          }
        %>
      </div>
    </div>

    <div class="spec">
      <h3>About this item</h3>
      <p><%= description %></p>
    </div>
  </section>

  <aside class="buybox card">
    <div class="buy-price">
      <div class="amount"><span class="big"><%= Math.round(price) %></span> SEK</div>
    </div>

    <div class="stock">
      <div class="<%= stockClass %>"><%= stockText %></div>
    </div>

    <form action="<%= request.getContextPath() %>/cart-update.jsp" method="post" class="stack">
      <input type="hidden" name="op" value="add"> <!-- 👈 detta saknas -->
      <input type="hidden" name="productId" value="<%= item.getId() %>">

      <!-- dessa behövs inte för själva serverlogiken, kan tas bort om du vill -->
      <input type="hidden" name="name" value="<%= name %>">
      <input type="hidden" name="price" value="<%= price %>">

      <label>Quantity
        <select name="qty" <%= stock <= 0 ? "disabled" : "" %>>
          <%
            if (stock <= 0) {
          %>
              <option value="0" disabled>Out of stock</option>
          <%
            } else {
                for (int i = 1; i <= maxSelectable; i++) {
          %>
                <option value="<%= i %>"><%= i %></option>
          <%
                }
            }
          %>
        </select>
      </label>

      <button type="submit" <%= stock <= 0 ? "disabled class='btn-disabled'" : "" %>>
        Add to cart
      </button>
    </form>

    <%
      if (stock > 0 && stock <= 10) {
    %>
      <div class="small-muted">Only <strong><%= stock %></strong> left.</div>
    <%
      } else if (stock > 10) {
    %>
      <div class="small-muted">You can select up to 10 at a time.</div>
    <%
      }
    %>

    <%
      String added = request.getParameter("added");
      String addErr = request.getParameter("adderror");
      if ("1".equals(added)) {
    %>
      <section class="card" style="margin-bottom:12px;">
        <strong>Added to cart.</strong>
      </section>
    <%
      } else if (addErr != null) {
    %>
      <section class="card" style="margin-bottom:12px;">
        <strong>Could not add to cart:</strong> <%= addErr %>
      </section>
    <%
      }
    %>
  </aside>
<%
  }
%>
</main>

</body>
</html>