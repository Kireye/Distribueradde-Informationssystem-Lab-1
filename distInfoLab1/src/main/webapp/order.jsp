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

<%@ page import="com.werkstrom.distinfolab1.ui.UserInfo" %>
<%
    UserInfo u = null;
    if (session != null) {
        Object o = session.getAttribute("user");
        if (o instanceof UserInfo) {
            u = (UserInfo) o;
        }
    }
%>

<main class="container">
  <h1>Mina ordrar</h1>

  <%
    if (u == null) {
  %>
      <p>Du är inte inloggad.</p>
      <p><a href="<%= request.getContextPath() %>/login.jsp">Gå till inloggning</a></p>
  <%
    } else {
  %>
      <div class="card">
        <h3>Hej, <%= u.getName() %>!</h3>
        <p class="muted">E-post: <%= u.getEmail() %></p>
      </div>

      <%
        java.util.List<com.werkstrom.distinfolab1.bo.Order> orders = u.getOrders();
        if (orders == null || orders.isEmpty()) {
      %>
          <p>Du har inga ordrar ännu.</p>
      <%
        } else {
            for (int i = 0; i < orders.size(); i++) {
                com.werkstrom.distinfolab1.bo.Order o = orders.get(i);
      %>
          <div class="card">
            <h3>Order #<%= o.getOrderId() %></h3>
            <p>Status: <%= o.getStatus() %></p>
            <p>Antal rader: <%= o.getNrOfItems() %></p>
          </div>
      <%
            }
        }
      %>
  <%
    }
  %>
</main>

</body>
</html>