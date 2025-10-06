<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="sv">
<head>
  <meta charset="UTF-8">
  <title>Logga in – MiniShop</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<%@ include file="/WEB-INF/jspf/header.jspf" %>

<main class="container auth-container">
  <section class="auth-box card">
    <h1>Logga in</h1>

    <p class="muted" style="${empty requestScope.error ? 'display:none' : 'color:#dc2626'}">
      ${requestScope.error}
    </p>

    <!-- POST till UserServlet -->
    <form action="${pageContext.request.contextPath}/user/login" method="post" class="stack">
      <label>E-post
        <div class="input-icon">
          <svg viewBox="0 0 24 24" aria-hidden="true">
            <path d="M12 12a5 5 0 1 0-5-5 5 5 0 0 0 5 5Zm0 2c-5.33 0-8 2.67-8 5v1h16v-1c0-2.33-2.67-5-8-5Z"/>
          </svg>
          <input type="email" name="email" placeholder="E-postadress" required>
        </div>
      </label>

      <label>Lösenord
        <div class="input-icon">
          <svg viewBox="0 0 24 24" aria-hidden="true">
            <path d="M17 9h-1V7a4 4 0 0 0-8 0v2H7a2 2 0 0 0-2 2v8a2 2 0 0 0 2 2h10a2 2 0 0 0 2-2v-8a2 2 0 0 0-2-2Zm-6 0V7a3 3 0 0 1 6 0v2Zm3 7a1.5 1.5 0 1 1 1.5-1.5A1.5 1.5 0 0 1 14 16Z"/>
          </svg>
          <input type="password" name="password" placeholder="Lösenord" required>
        </div>
      </label>

      <button type="submit" class="btn-block">Logga in</button>
    </form>

    <div class="auth-links center">
      <a class="link" href="${pageContext.request.contextPath}/user/register">Bli medlem</a>
    </div>
  </section>
</main>

</body>
</html>