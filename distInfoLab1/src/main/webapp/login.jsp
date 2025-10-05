<%@ include file="/WEB-INF/jspf/db-init.jspf" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.werkstrom.distinfolab1.bo.facades.UserFacade" %>
<%@ page import="com.werkstrom.distinfolab1.ui.UserInfo" %>
<%@ page import="com.werkstrom.distinfolab1.db.MySqlConnectionManager" %>

<%
    if ("POST".equalsIgnoreCase(request.getMethod())) {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        boolean hasError = false;
        String errorMsg = null;

        if (email == null || email.isEmpty()) {
            hasError = true;
            errorMsg = "Email must not be empty.";
        }

        if (!hasError) {
            if (password == null || password.isEmpty()) {
                hasError = true;
                errorMsg = "Password must not be empty.";
            }
        }

        if (!hasError) {
            try {
                UserInfo user = UserFacade.login(email, password);
                session.setAttribute("user", user);
                response.sendRedirect(request.getContextPath() + "/order.jsp");
                return;
            } catch (Exception ex) {
                request.setAttribute("loginError", "Login failed: " + ex.getMessage());
                try { MySqlConnectionManager.rollbackTransaction(); } catch (Exception ignored) { }
            }
        } else {
            request.setAttribute("loginError", errorMsg);
        }
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Login</title>
  <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>
<body>

<%@ include file="/WEB-INF/jspf/header.jspf" %>

<main class="container auth-container">
  <section class="auth-box card">
    <h1>Login</h1>

    <%
      Object err = request.getAttribute("loginError");
      if (err != null) {
    %>
      <div class="alert error"><%= err %></div>
    <%
      }
    %>

    <form action="<%= request.getContextPath() %>/login.jsp" method="post" class="stack">
      <label>
        <span class="sr-only">Email</span>
        <div class="input-icon">
          <svg viewBox="0 0 24 24" aria-hidden="true">
            <path d="M12 12a5 5 0 1 0-5-5 5 5 0 0 0 5 5Zm0 2c-5.33 0-8 2.67-8 5v1h16v-1c0-2.33-2.67-5-8-5Z"/>
          </svg>
          <input type="email" name="email" placeholder="Email address" required>
        </div>
      </label>

      <label>
        <span class="sr-only">Password</span>
        <div class="input-icon">
          <svg viewBox="0 0 24 24" aria-hidden="true">
            <path d="M17 9h-1V7a4 4 0 0 0-8 0v2H7a2 2 0 0 0-2 2v8a2 2 0 0 0 2 2h10a2 2 0 0 0 2-2v-8a2 2 0 0 0-2-2Zm-6 0V7a3 3 0 0 1 6 0v2Zm3 7a1.5 1.5 0 1 1 1.5-1.5A1.5 1.5 0 0 1 14 16Z"/>
          </svg>
          <input type="password" name="password" placeholder="Password" required>
        </div>
      </label>

      <button type="submit" class="btn-block">Login</button>
    </form>

    <div class="auth-links center">
      <a class="link" href="<%= request.getContextPath() %>/register.jsp">Create account</a>
    </div>
  </section>
</main>

</body>
</html>