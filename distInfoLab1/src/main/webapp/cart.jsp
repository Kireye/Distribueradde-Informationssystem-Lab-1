<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="sv">
<head>
  <meta charset="UTF-8">
  <title>Varukorg</title>
  <link rel="stylesheet" href="css/style.css">
</head>
<body>

<%@ include file="/WEB-INF/jspf/header.jspf" %>

<main class="container">
  <h1>Varukorg</h1>
  <p class="muted">
    Den här sidan visar korgen. När CartController är klar kan den lägga en lista i
    <code>request</code> eller <code>session</code> som du loopar på med JSTL/EL.
  </p>

  <!-- Placeholder-tabell (ersätt senare med riktig data) -->
  <table class="table">
    <thead>
      <tr>
        <th>Produkt</th>
        <th>Pris</th>
        <th>Antal</th>
        <th>Delsumma</th>
      </tr>
    </thead>
    <tbody>
      <tr>
        <td>Exempelprodukt</td>
        <td>199 kr</td>
        <td>2</td>
        <td>398 kr</td>
      </tr>
    </tbody>
    <tfoot>
      <tr>
        <th colspan="3" class="right">Totalt</th>
        <th>398 kr</th>
      </tr>
    </tfoot>
  </table>

  <div class="row gap">
    <!-- Uppdatera antal senare (CartController /cart/update) -->
    <form action="<%= request.getContextPath() %>/cart/clear" method="post">
      <button type="submit" class="btn-secondary">Töm korg</button>
    </form>

    <!-- Checkout leder till OrderController (t.ex. /orders/checkout) -->
    <form action="<%= request.getContextPath() %>/orders/checkout" method="post">
      <button type="submit">Till kassan</button>
    </form>
  </div>
</main>

</body>
</html>