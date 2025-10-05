<%@ include file="/WEB-INF/jspf/db-init.jspf" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.werkstrom.distinfolab1.bo.facades.ItemFacade" %>
<%@ page import="com.werkstrom.distinfolab1.ui.ItemInfo" %>

<!DOCTYPE html>
<html lang="sv">
<head>
  <meta charset="UTF-8">
  <title>MiniShop</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<%@ include file="/WEB-INF/jspf/header.jspf" %>

<%
  // Läs filter från query
  String q = request.getParameter("q");               // text-sökning
  String catIdParam = request.getParameter("catId");  // numerisk kategori-id (valfritt)
  Integer catId = null;
  if (catIdParam != null) {
      try {
          catId = Integer.valueOf(catIdParam);
      } catch (NumberFormatException ignored) { }
  }

  java.util.List<ItemInfo> items = java.util.Collections.emptyList();
  String loadError = null;

  try {
      // Hämta från facaden (true = endast i lager; byt till false om du vill visa slut)
      items = ItemFacade.search(q, catId, false);
  } catch (Exception e) {
      loadError = e.getMessage();
  }
%>

<main class="container">
  <h1>Produkter</h1>

  <%
    if (loadError != null) {
  %>
      <p class="muted">Kunde inte ladda produkter: <%= loadError %></p>
  <%
    } else if (items == null || items.isEmpty()) {
  %>
      <p class="muted">Inga produkter hittades.</p>
  <%
    } else {
  %>
      <section class="grid">
        <%
          for (ItemInfo it : items) {
              String statusText = "Slut";
              String mutedClass = "muted";
              if (it.getStock() > 10) {
                  statusText = "I lager";
              } else if (it.getStock() > 0) {
                  statusText = "Få kvar";
              }

              String imgName = "placeholder.jpg"; // Byt när du har bildfält i DB
              String nameEsc = it.getName();      // Enkelt tills vidare
        %>
          <article class="card">
            <a href="<%= request.getContextPath() %>/item.jsp?id=<%= it.getId() %>">
              <img src="<%= request.getContextPath() %>/images/<%= imgName %>" alt="<%= nameEsc %>" class="thumb">
            </a>
            <h3>
              <a href="<%= request.getContextPath() %>/item.jsp?id=<%= it.getId() %>"><%= nameEsc %></a>
            </h3>
            <p class="price"><%= Math.round(it.getPrice()) %> kr</p>
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
