<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="sv">
<head>
  <meta charset="UTF-8">
  <title>MiniShop – Produkter</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<%@ include file="/WEB-INF/jspf/header.jspf" %>

<main class="container">
  <h1>Produkter</h1>
  <p class="muted">Klicka på en produkt för att se mer information och lägga till i kundvagnen.</p>

  <!--
    Denna sida är VY endast.
    ItemServlet lägger färdigrenderade kort i requestScope.itemsHtml.
    Vid fel läggs ett kort med feltext.
    TODO: När vi bygger item-detaljsida, länkarna i ItemServlets HTML byts till /items/detail?id=...
  -->

  <section class="grid">
    ${requestScope.itemsHtml}
  </section>
</main>

</body>
</html>
