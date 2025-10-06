<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="sv">
<head>
  <meta charset="UTF-8">
  <title>MiniShop</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<%@ include file="/WEB-INF/jspf/header.jspf" %>

<main class="container">
  <h1>Produkter</h1>
  <p class="muted">Klicka på en produkt för att se mer information och lägga till i kundvagnen.</p>

  <!-- Färdigrenderade kort injiceras av ItemServlet -->
  <section class="grid">
    ${requestScope.itemsHtml}
  </section>
</main>

</body>
</html>