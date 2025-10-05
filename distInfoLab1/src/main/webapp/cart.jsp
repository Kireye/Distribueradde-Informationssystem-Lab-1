<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Cart</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<%@ include file="/WEB-INF/jspf/header.jspf" %>

<main class="container">
  <h1>Cart</h1>

  <table class="table">
    <thead>
      <tr>
        <th>Product</th>
        <th>Price</th>
        <th>Qty</th>
        <th>Subtotal</th>
      </tr>
    </thead>
    <tbody>
      <tr>
        <td>Sample product</td>
        <td>199 SEK</td>
        <td>2</td>
        <td>398 SEK</td>
      </tr>
    </tbody>
    <tfoot>
      <tr>
        <th colspan="3" class="right">Total</th>
        <th>398 SEK</th>
      </tr>
    </tfoot>
  </table>

  <div class="row gap">
    <form action="${pageContext.request.contextPath}/cart/clear" method="post">
      <button type="submit" class="btn-secondary">Clear cart</button>
    </form>

    <a href="<%= request.getContextPath() %>/checkout.jsp">
      <button type="button">Checkout</button>
    </a>
  </div>
</main>

</body>
</html>