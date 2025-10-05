<%@ include file="/WEB-INF/jspf/db-init.jspf" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.werkstrom.distinfolab1.bo.facades.ItemFacade" %>
<%@ page import="com.werkstrom.distinfolab1.ui.ItemInfo" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>MiniShop</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<%@ include file="/WEB-INF/jspf/header.jspf" %>

<%
  String q = request.getParameter("q");
  String catIdParam = request.getParameter("catId");
  String categoryName = request.getParameter("category"); // legacy textual filter (sv/en)

  Integer catId = null;

  if (catIdParam != null) {
      try {
          int parsed = Integer.parseInt(catIdParam);
          if (parsed > 0) catId = parsed;
      } catch (NumberFormatException ignored) {}
  }

  // Optional: accept English names as well (fallback)
  if (catId == null && categoryName != null) {
      String c = categoryName.trim().toLowerCase();
      if ("electronics".equals(c) || "elektronik".equals(c)) catId = 1;
      else if ("home & kitchen".equals(c) || "hem & kök".equals(c)) catId = 2;
      else if ("video games & consoles".equals(c) || "tv-spel & konsoler".equals(c)) catId = 3;
      else if ("books".equals(c) || "böcker".equals(c)) catId = 4;
      else if ("toys & games".equals(c) || "leksaker & spel".equals(c)) catId = 5;
      else if ("sport & outdoor".equals(c)) catId = 6;
      else if ("bygg, el & verktyg".equals(c) || "tools".equals(c)) catId = 7;
  }

  java.util.List<ItemInfo> items = java.util.Collections.emptyList();
  String loadError = null;

  try {
      items = ItemFacade.search(q, catId, false);
  } catch (Exception e) {
      loadError = e.getMessage();
  }
%>

<main class="container">
  <h1>Products</h1>

  <%
    if (catId != null) {
  %>
      <p class="muted">Filter: <strong>Category #<%= catId %></strong></p>
  <%
    } else if (q != null && !q.isEmpty()) {
  %>
      <p class="muted">Search: <strong><%= q %></strong></p>
  <%
    }
  %>

  <%
    if (loadError != null) {
  %>
      <p class="muted">Could not load products: <%= loadError %></p>
  <%
    } else if (items == null || items.isEmpty()) {
  %>
      <p class="muted">No products found.</p>
  <%
    } else {
  %>
      <section class="grid">
        <%
          for (ItemInfo it : items) {
              String statusText = "Out of stock";
              String mutedClass = "muted";
              if (it.getStock() > 10) {
                  statusText = "In stock";
              } else if (it.getStock() > 0) {
                  statusText = "Low stock";
              }
        %>
          <article class="card">
            <a href="<%= request.getContextPath() %>/item.jsp?id=<%= it.getId() %>">
              <img src="<%= request.getContextPath() %>/images/placeholder.jpg" alt="<%= it.getName() %>" class="thumb">
            </a>
            <h3>
              <a href="<%= request.getContextPath() %>/item.jsp?id=<%= it.getId() %>"><%= it.getName() %></a>
            </h3>
            <p class="price"><%= Math.round(it.getPrice()) %> SEK</p>
            <p class="<%= mutedClass %>"><%= statusText %></p>
          </article>
        <%
          }
        %>
      </section>
  <%
    }
  %>
</main>

</body>
</html>