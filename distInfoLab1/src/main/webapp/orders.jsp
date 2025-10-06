<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="sv">
<head>
  <meta charset="UTF-8">
  <title>Mina ordrar – MiniShop</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<%@ include file="/WEB-INF/jspf/header.jspf" %>

<main class="container">
  <h1>Mina ordrar</h1>
  <p class="muted" style="${empty requestScope.error ? 'display:none' : 'color:#dc2626'}">
    ${requestScope.error}
  </p>

  <section>
    ${requestScope.ordersHtml}
  </section>
</main>

</body>
</html>
