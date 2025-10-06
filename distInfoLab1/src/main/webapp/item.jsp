<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="sv">
<head>
  <meta charset="UTF-8">
  <title>${requestScope.item.name} – MiniShop</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<%@ include file="/WEB-INF/jspf/header.jspf" %>

<main class="container">
  <!-- Denna vy förutsätter att ItemServlet /items/detail?id=... satt alla request-attribut -->
  <section class="product-page">
    <img class="main-image"
         src="${requestScope.imageUrl}"
         alt="${requestScope.item.name}"
         onerror="this.onerror=null;this.src='${pageContext.request.contextPath}/images/placeholder.png';">

    <div>
      <h1 class="product-title">${requestScope.item.name}</h1>
      <div class="meta">
        <span class="muted">${requestScope.categoryLine}</span>
      </div>
      <div class="spec">
        <h3>Beskrivning</h3>
        <p class="muted">${requestScope.item.description}</p>
      </div>
    </div>

    <aside class="buybox card">
      <div class="buy-price">
        <span class="amount big">${requestScope.price} kr</span>
      </div>

      <div class="stock">
        <span class="${requestScope.stockClass}">${requestScope.stockText}</span>
      </div>

      <!-- TODO: implementera CartServlet: POST /cart/add -->
      <form action="${pageContext.request.contextPath}/cart/add" method="post" class="stack">
        <input type="hidden" name="itemId" value="${requestScope.item.id}">
        <label>Antal
          <input type="number" name="qty"
                 min="1" max="${requestScope.qtyMax}"
                 value="${requestScope.qtyDefault}"
                 ${requestScope.qtyInputAttrs}>
        </label>
        <button type="submit" ${requestScope.addBtnAttrs}>Lägg i varukorg</button>
      </form>

      <p class="small-muted">Fri retur inom 30 dagar.</p>
    </aside>
  </section>
</main>

</body>
</html>