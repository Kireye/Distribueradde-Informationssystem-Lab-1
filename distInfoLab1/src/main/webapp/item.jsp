<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="sv">
<head>
  <meta charset="UTF-8">
  <title>${param.name} – Produkt</title>
  <link rel="stylesheet" href="css/style.css">
</head>
<body>

<%@ include file="/WEB-INF/jspf/header.jspf" %>

<%
  String ctx = request.getContextPath();

  // Hämta bildfil från query (fallback till airpods.jpg om inget skickas)
  String imageFile = request.getParameter("image");
  if (imageFile == null || imageFile.isBlank()) {
      imageFile = "espressomachine.jpg";
  }

  // Beräkna lagertext/klass
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
        <input type="number" name="qty" min="1" value="1" <%= s == 0 ? "disabled" : "" %>>
      </label>
      <button type="submit" <%= s == 0 ? "disabled class='btn-disabled'" : "" %>>
        Lägg till i kundvagn
      </button>
    </form>

    <div class="small-muted">Leveransinfo, returer m.m. (fylls i senare).</div>
  </aside>

</main>

</body>
</html>
