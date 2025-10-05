<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    // --- Workaround/temporär “betalning” ---
    boolean paid = false;
    String paymentMethod = null;

    if ("POST".equalsIgnoreCase(request.getMethod())) {
        paymentMethod = request.getParameter("payment"); // "card" eller "invoice"
        if (paymentMethod == null || paymentMethod.isBlank()) paymentMethod = "card";

        // Använd JSP:s inbyggda 'session' (implicit object) – skapa om den saknas
        if (session == null) {
            session = request.getSession(true);
        }

        Integer cartCount = (Integer) session.getAttribute("cartCount");
        if (cartCount == null) cartCount = 0;

        Integer ordersCount = (Integer) session.getAttribute("ordersCount");
        if (ordersCount == null) ordersCount = 0;

        if (cartCount > 0) {
            ordersCount = ordersCount + 1;
            session.setAttribute("ordersCount", ordersCount);
            session.setAttribute("lastOrderPayment", paymentMethod);
            session.setAttribute("lastOrderTime", new java.util.Date());
        }

        // Töm kundvagnens badge (simulerar att varorna lades i en order)
        session.setAttribute("cartCount", 0);

        paid = true;
        request.setAttribute("paid", true);
    }
%>
<!DOCTYPE html>
<html lang="sv">
<head>
  <meta charset="UTF-8">
  <title>Kassa</title>
  <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css?v=9">
</head>
<body>

<%@ include file="/WEB-INF/jspf/header.jspf" %>

<main class="container">
  <h1>Kassa</h1>

  <%
    if (paid) {
  %>
    <!-- Bekräftelsevy efter "betalning" -->
    <section class="card" style="max-width: 560px;">
      <h2>Tack för din beställning!</h2>
      <p>Din betalningsmetod: <strong><%= "invoice".equals(paymentMethod) ? "Faktura" : "Kort" %></strong></p>
      <p class="muted">Detta är en demo – senare kommer ordern sparas i databasen och visas under Ordrar.</p>

      <div class="row" style="margin-top:12px;">
        <a href="<%= request.getContextPath() %>/order.jsp">
          <button type="button">Visa mina ordrar</button>
        </a>
        <a href="<%= request.getContextPath() %>/index.jsp">
          <button type="button" class="btn-secondary">Fortsätt handla</button>
        </a>
      </div>
    </section>
  <%
    } else {
  %>
    <!-- Formulärvy innan betalning -->
    <section class="grid-2">
      <article class="card">
        <h2>Betalning</h2>
        <form method="post" class="stack">
          <label class="row" style="align-items:center;">
            <input type="radio" name="payment" value="card" checked>
            <span>Kort</span>
          </label>
          <label class="row" style="align-items:center;">
            <input type="radio" name="payment" value="invoice">
            <span>Faktura</span>
          </label>

          <!-- TODO: När riktig logik finns, visa/validera fält baserat på valt betalningssätt -->

          <button type="submit">Betala</button>
        </form>
      </article>

      <aside class="card">
        <h2>Orderöversikt</h2>
        <p class="muted">Här kan du visa radartiklar från kundvagnen när modellen är klar.</p>
        <!-- TODO: Iterera över sessionens cart-items när BO/DB är på plats -->
        <ul style="margin:0; padding-left:18px;">
          <li>Exempelprodukt 1 (x1)</li>
          <li>Exempelprodukt 2 (x2)</li>
        </ul>
        <p style="margin-top:8px;"><strong>Summa:</strong> (fylls i senare)</p>
      </aside>
    </section>
  <%
    }
  %>
</main>

</body>
</html>