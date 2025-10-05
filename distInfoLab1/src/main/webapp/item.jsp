<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="sv">
<head>
  <meta charset="UTF-8">
  <title>${param.name} – Produkt</title>
  <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css?v=10">
</head>
<body>

<%@ include file="/WEB-INF/jspf/header.jspf" %>

<%
  String ctx = request.getContextPath();

  // Bildfil från query (fallback)
  String imageFile = request.getParameter("image");
  if (imageFile == null || imageFile.isBlank()) {
      imageFile = "airpods.jpg";
  }

  // Lager
  String stockParam = request.getParameter("stock");
  int s = 0;
  if (stockParam != null) {
      try { s = Integer.parseInt(stockParam); } catch (NumberFormatException ignored) {}
  }

  String stockText;
  String stockClass;
  if (s > 10) {
      stockText = "I lager";
      stockClass = "in-stock";
  } else if (s > 0) {
      stockText = "Få kvar";
      stockClass = "low-stock";
  } else {
      stockText = "Slut";
      stockClass = "out-stock";
  }

  // Hur många val ska dropdownen visa?
  // Nu: upp till s (lager), men max 10 för att inte bli för lång.
  // TODO: När DB/BO finns, sätt exakt från produktens lagerfält.
  int maxSelectable = Math.min(Math.max(s, 0), 10);
%>

<main class="container product-page">

  <!-- Vänster: huvudbild -->
  <section class="gallery">
    <img src="<%= ctx %>/images/<%= imageFile %>" alt="${param.name}" class="main-image">
  </section>

  <!-- Mitten: detaljer -->
  <section class="details">
    <h1 class="product-title">${param.name}</h1>

    <div class="meta">
      <div><span class="muted">Kategori:</span> ${param.category}</div>
      <div><span class="muted">Artikelnummer:</span> ${param.id}</div>
    </div>

    <div class="spec">
      <h3>Om denna artikel</h3>
      <ul>
        <li>Här kan du visa en beskrivning från databasen.</li>
        <li>Produktens egenskaper, t.ex. material, färg eller modell.</li>
        <li>Exempelvis leveranstid och tillverkare.</li>
      </ul>
    </div>
  </section>

  <!-- Höger: köpbox -->
  <aside class="buybox card">
    <div class="buy-price">
      <div class="amount"><span class="big">${param.price}</span> kr</div>
    </div>

    <div class="stock">
      <div class="<%= stockClass %>"><%= stockText %></div>
    </div>

    <form action="<%= ctx %>/cart/add" method="post" class="stack">
      <input type="hidden" name="productId" value="${param.id}">
      <input type="hidden" name="name" value="${param.name}">
      <input type="hidden" name="price" value="${param.price}">

      <label>Antal
        <select name="qty" <%= s == 0 ? "disabled" : "" %> >
          <%
            // Generera 1..maxSelectable. Om s == 0, rendera ett disabled val.
            if (s <= 0) {
          %>
              <option value="0" disabled>Slut i lager</option>
          <%
            } else {
              for (int i = 1; i <= maxSelectable; i++) {
          %>
                <option value="<%= i %>"><%= i %></option>
          <%
              }
              // Om vi kapat på 10, ge en hint
              if (s > 10) {
          %>
                <!-- Visar bara upp till 10 i dropdownen just nu -->
          <%
              }
            }
          %>
        </select>
      </label>

      <button type="submit" <%= s == 0 ? "disabled class='btn-disabled'" : "" %>>
        Lägg till i kundvagn
      </button>
    </form>

    <%
      if (s > 0 && s <= 10) {
    %>
      <div class="small-muted">Endast <strong><%= s %></strong> kvar.</div>
    <%
      } else if (s > 10) {
    %>
      <div class="small-muted">Välj upp till 10 åt gången.</div>
    <%
      }
    %>
  </aside>

</main>

</body>
</html>