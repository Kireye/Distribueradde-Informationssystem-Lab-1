<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="sv">
<head>
  <meta charset="UTF-8">
  <title>Mina ordrar</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<%@ include file="/WEB-INF/jspf/header.jspf" %>

<main class="container">
  <h1>Mina ordrar</h1>
  <p class="muted">
    Låt OrderController kontrollera inloggning (t.ex. via <code>session.userEmail</code>).
    Om inte inloggad → <code>response.sendRedirect("login.jsp")</code>.
  </p>

  <div class="card">
    <h3>Order #12345</h3>
    <p>Datum: 2025-10-04 • Summa: 398 kr • Status: Skickad</p>
  </div>
  <div class="card">
    <h3>Order #12312</h3>
    <p>Datum: 2025-09-21 • Summa: 249 kr • Status: Levererad</p>
  </div>
</main>

</body>
</html>