<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="sv">
<head>
  <meta charset="UTF-8">
  <title>Skapa konto</title>
  <link rel="stylesheet" href="css/style.css">
</head>
<body>

<%@ include file="/WEB-INF/jspf/header.jspf" %>

<main class="container auth-container">
  <section class="auth-box card">
    <h1>Skapa konto</h1>

    <!-- När du bygger controller: POST /users -->
    <form action="<%= request.getContextPath() %>/users" method="post" class="stack">
      <label>
        <span class="sr-only">E-post</span>
        <div class="input-icon">
          <svg viewBox="0 0 24 24" aria-hidden="true">
            <path d="M12 12a5 5 0 1 0-5-5 5 5 0 0 0 5 5Zm0 2c-5.33 0-8 2.67-8 5v1h16v-1c0-2.33-2.67-5-8-5Z"/>
          </svg>
          <input type="email" name="email" placeholder="E-postadress" required>
        </div>
      </label>

      <label>
        <span class="sr-only">Lösenord</span>
        <div class="input-icon">
          <svg viewBox="0 0 24 24" aria-hidden="true">
            <path d="M17 9h-1V7a4 4 0 0 0-8 0v2H7a2 2 0 0 0-2 2v8a2 2 0 0 0 2 2h10a2 2 0 0 0 2-2v-8a2 2 0 0 0-2-2Zm-6 0V7a3 3 0 0 1 6 0v2Zm3 7a1.5 1.5 0 1 1 1.5-1.5A1.5 1.5 0 0 1 14 16Z"/>
          </svg>
          <input type="password" name="password" placeholder="Lösenord" required>
        </div>
      </label>

      <button type="submit" class="btn-block">Skapa konto</button>
    </form>

    <div class="auth-links">
      <a class="link" href="<%= request.getContextPath() %>/login.jsp">Har du redan konto? Logga in</a>
    </div>
  </section>
</main>

</body>
</html>
