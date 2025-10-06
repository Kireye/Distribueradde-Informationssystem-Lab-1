<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="sv">
<head>
  <meta charset="UTF-8">
  <title>Varukorg – MiniShop</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<%@ include file="/WEB-INF/jspf/header.jspf" %>

<main class="container">
  <h1>Din varukorg</h1>
  <p class="muted" style="${empty requestScope.error ? 'display:none' : 'color:#dc2626'}">
    ${requestScope.error}
  </p>

  <section>
    ${requestScope.cartHtml}
  </section>
</main>

</body>
</html>
