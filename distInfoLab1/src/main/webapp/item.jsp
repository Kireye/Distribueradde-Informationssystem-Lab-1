<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="sv">
<head>
  <meta charset="UTF-8">
  <title>${param.name} – MiniShop</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<%@ include file="/WEB-INF/jspf/header.jspf" %>

<main class="container">
  <!-- TODO: När ItemServlet finns:
       - Ladda denna sida via /items/{id}
       - Servleten sätter requestScope.item (ItemInfo) → byt ${param.*} till ${requestScope.item.*} -->
  <section class="product-page">
    <img class="main-image"
         src="${pageContext.request.contextPath}/images/${param.image}"
         alt="${param.name}">

    <div>
      <h1 class="product-title">${param.name}</h1>
      <div class="meta">
        <span class="muted">${param.category}</span>
      </div>

      <div class="spec">
        <h3>Beskrivning</h3>
        <p class="muted">Detaljerad beskrivning kommer här.</p>
      </div>
    </div>

    <aside class="buybox card">
      <div class="buy-price">
        <span class="amount big">${param.price} kr</span>
      </div>

      <div class="stock">
        <span class="${param.stock gt 5 ? 'in-stock' : (param.stock gt 0 ? 'low-stock' : 'out-stock')}">
          ${param.stock gt 0 ? 'I lager' : 'Slut'}
        </span>
      </div>

      <!-- TODO: POST till /cart/add när CartServlet finns.
           Skicka itemId och quantity. -->
      <form action="${pageContext.request.contextPath}/cart/add" method="post" class="stack">
        <input type="hidden" name="itemId" value="${param.id}">
        <label>Antal
          <input type="number" name="qty" min="1" max="${param.stock}" value="${param.stock gt 0 ? 1 : 0}" ${param.stock gt 0 ? '' : 'disabled'}>
        </label>
        <button type="submit" ${param.stock gt 0 ? '' : 'class="btn-disabled" disabled'}>Lägg i varukorg</button>
      </form>

      <p class="small-muted">Fri retur inom 30 dagar.</p>
    </aside>
  </section>
</main>

</body>
</html>