<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    boolean paid = false;
    String paymentMethod = null;

    if ("POST".equalsIgnoreCase(request.getMethod())) {
        paymentMethod = request.getParameter("payment");
        if (paymentMethod == null || paymentMethod.isBlank()) paymentMethod = "card";

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

        session.setAttribute("cartCount", 0);

        paid = true;
        request.setAttribute("paid", true);
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Checkout</title>
  <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css?v=9">
</head>
<body>

<%@ include file="/WEB-INF/jspf/header.jspf" %>

<main class="container">
  <h1>Checkout</h1>

  <%
    if (paid) {
  %>
    <section class="card" style="max-width: 560px;">
      <h2>Thank you for your order!</h2>
      <p>Payment method: <strong><%= "invoice".equals(paymentMethod) ? "Invoice" : "Card" %></strong></p>
      <p class="muted">This is a demo — later the order will be stored in the database and shown under Orders.</p>

      <div class="row" style="margin-top:12px;">
        <a href="<%= request.getContextPath() %>/order.jsp">
          <button type="button">View my orders</button>
        </a>
        <a href="<%= request.getContextPath() %>/index.jsp">
          <button type="button" class="btn-secondary">Continue shopping</button>
        </a>
      </div>
    </section>
  <%
    } else {
  %>
    <section class="grid-2">
      <article class="card">
        <h2>Payment</h2>
        <form method="post" class="stack">
          <label class="row" style="align-items:center;">
            <input type="radio" name="payment" value="card" checked>
            <span>Card</span>
          </label>
          <label class="row" style="align-items:center;">
            <input type="radio" name="payment" value="invoice">
            <span>Invoice</span>
          </label>

          <button type="submit">Pay</button>
        </form>
      </article>

      <aside class="card">
        <h2>Order summary</h2>
        <p class="muted">Display cart line items here when the model is ready.</p>
        <ul style="margin:0; padding-left:18px;">
          <li>Sample product 1 (x1)</li>
          <li>Sample product 2 (x2)</li>
        </ul>
        <p style="margin-top:8px;"><strong>Total:</strong> (to be filled later)</p>
      </aside>
    </section>
  <%
    }
  %>
</main>

</body>
</html>