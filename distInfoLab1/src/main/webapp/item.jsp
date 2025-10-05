<%@ include file="/WEB-INF/jspf/db-init.jspf" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.werkstrom.distinfolab1.bo.facades.ItemFacade" %>
<%@ page import="com.werkstrom.distinfolab1.ui.ItemInfo" %>
<!DOCTYPE html>
<html lang="sv">
<head>
  <meta charset="UTF-8">
  <title>Produkt</title>
  <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css?v=11">
</head>
<body>

<%@ include file="/WEB-INF/jspf/header.jspf" %>

<%
  String idParam = request.getParameter("id");
  ItemInfo item = null;
  String err = null;
  if (idParam == null) {
      err = "Ingen produkt-id angiven.";
  } else {
      try {
          int itemId = Integer.parseInt(idParam);
          item = ItemFacade.getItemById(itemId);
      } catch (NumberFormatException nfe) {
          err = "Ogiltigt produkt-id.";
      } catch (Exception e) {
          err = "Kunde inte hämta produkt: " + e.getMessage();
      }
  }
%>

<main class="container product-page">
<%
  if (err != null) {
%>
    <section class="card">
      <h2>Fel</h2>
      <p><%= err %></p>
      <p><a href="<%= request.getContextPath() %>/index.jsp">Tillbaka till produkter</a></p>
    </section>
<%
  } else if (item == null) {
%>
    <section class="card">
      <p>Produkten kunde inte hittas.</p>
      <p><a href="<%= request.getContextPath() %>/index.jsp">Tillbaka till produkter</a></p>
    </section>
<%
  } else {
      String name = item.getName();
      String description = item.getDescription();
      float price = item.getPrice();
      int stock = item.getStock();

      String stockText = "Slut";
      String stockClass = "out-stock";
      if (stock > 10) {
          stockText = "I lager";
          stockClass = "in-stock";
      } else if (stock > 0) {
          stockText = "Få kvar";
          stockClass = "low-stock";
      }

      int maxSelectable = stock;
      if (maxSelectable > 10) {
          maxSelectable = 10;
      }
%>

  <!-- Vänster: huvudbild -->
  <section class="gallery">
    <img src="<%= request.getContextPath() %>/images/placeholder.jpg" alt="<%= name %>" class="main-image">
  </section>

  <!-- Mitten: detaljer -->
  <section class="details">
    <h1 class="product-title"><%= name %></h1>

    <div class="meta">
      <div><span class="muted">Artikelnummer:</span> <%= item.getId() %></div>
      <div><span class="muted">Kategorier:</span>
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
      <h3>Om denna artikel</h3>
      <p><%= description %></p>
    </div>
  </section>

  <!-- Höger: köpbox -->
  <aside class="buybox card">
    <div class="buy-price">
      <div class="amount"><span class="big"><%= Math.round(price) %></span> kr</div>
    </div>

    <div class="stock">
      <div class="<%= stockClass %>"><%= stockText %></div>
    </div>

    <!-- TODO: Byt till /cart/add servlet senare -->
    <form action="<%= request.getContextPath() %>/cart/add" method="post" class="stack">
      <input type="hidden" name="productId" value="<%= item.getId() %>">
      <input type="hidden" name="name" value="<%= name %>">
      <input type="hidden" name="price" value="<%= price %>">

      <label>Antal
        <select name="qty" <%= stock <= 0 ? "disabled" : "" %>>
          <%
            if (stock <= 0) {
          %>
              <option value="0" disabled>Slut i lager</option>
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

      <button type="submit" <%
        if (stock <= 0) {
            out.print("disabled class='btn-disabled'");
        }
      %>>
        Lägg till i kundvagn
      </button>
    </form>

    <%
      if (stock > 0 && stock <= 10) {
    %>
      <div class="small-muted">Endast <strong><%= stock %></strong> kvar.</div>
    <%
      } else if (stock > 10) {
    %>
      <div class="small-muted">Välj upp till 10 åt gången.</div>
    <%
      }
    %>
  </aside>

<%
  } // end else item != null
%>
</main>

</body>
</html>