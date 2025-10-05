<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="sv">
<head>
  <meta charset="UTF-8">
  <title>${param.name} – Produkt</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<%@ include file="/WEB-INF/jspf/header.jspf" %>

<%
  // Bild (fallback)
  String imageFile = request.getParameter("image");
  if (imageFile == null || imageFile.trim().isEmpty()) {
      imageFile = "espressomachine.jpg";
  }

  // Lagerstatus -> text/klass
  int stockValue = 0;
  String stockParam = request.getParameter("stock");
  if (stockParam != null) {
      try { stockValue = Integer.parseInt(stockParam); } catch (NumberFormatException ignored) {}
  }

  String stockText;
  String stockClass;
  if (stockValue > 10) {
      stockText = "I lager";
      stockClass = "in-stock";
  } else if (stockValue > 0) {
      stockText = "Få kvar";
      stockClass = "low-stock";
  } else {
      stockText = "Slut";
      stockClass = "out-stock";
  }

  // Attribut till inputs/knapp (utan ternärer)
  boolean outOfStock = (stockValue == 0);
  String qtyDisabledAttr = "";
  String buttonDisabledAttr = "";
  String buttonClassAttr = "";
  if (outOfStock) {
      qtyDisabledAttr = "disabled";
      buttonDisabledAttr = "disabled";
      buttonClassAttr = "class='btn-disabled'";
  }
%>

<main class="container product-page">

  <!-- Vänster: huvudbild -->
  <section class="gallery">
    <img src="${pageContext.request.contextPath}/images/<%= imageFile %>" alt="${param.name}" class="main-image">
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

    <form action="${pageContext.request.contextPath}/cart/add" method="post" class="stack">
      <input type="hidden" name="productId" value="${param.id}">
      <input type="hidden" name="name" value="${param.name}">
      <input type="hidden" name="price" value="${param.price}">
      <label>Antal
        <input type="number" name="qty" min="1" value="1" <%= qtyDisabledAttr %>>
      </label>
      <button type="submit" <%= buttonDisabledAttr %> <%= buttonClassAttr %>>
        Lägg till i kundvagn
      </button>
    </form>

    <div class="small-muted">Leveransinfo, returer m.m. (fylls i senare).</div>
  </aside>

</main>

</body>
</html>