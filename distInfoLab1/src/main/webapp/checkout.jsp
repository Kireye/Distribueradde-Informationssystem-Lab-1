<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="sv">
<head>
  <meta charset="UTF-8">
  <title>Kassa – MiniShop</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
  <style>
    .paybox { background:#fff; border:1px solid #e5e7eb; border-radius:12px; padding:16px; }
    .payopt { display:flex; gap:8px; align-items:center; }
  </style>
</head>
<body>

<%@ include file="/WEB-INF/jspf/header.jspf" %>

<main class="container">
  <h1>Kassa</h1>

  <p class="muted" style="${empty requestScope.error ? 'display:none' : 'color:#dc2626'}">
    ${requestScope.error}
  </p>

  <!-- Vy-only: denna form POST:ar till OrderServlet /orders/place -->
  <section class="grid-2">
    <article class="paybox">
      <h2>Betalning</h2>

      <form action="${pageContext.request.contextPath}/orders/place" method="post" class="stack" id="checkoutForm">
        <label class="payopt">
          <input type="radio" name="payment" value="card" checked>
          <span>Kort</span>
        </label>

        <label class="payopt" style="margin-top:12px;">
          <input type="radio" name="payment" value="invoice">
          <span>Faktura</span>
        </label>

        <button type="submit" class="btn-block">Betala</button>
      </form>
    </article>

    <article class="paybox">
      <h2>Översikt</h2>
      <p class="muted">Ordern skapas när du klickar “Betala”.</p>
    </article>
  </section>
</main>

</body>
</html>
