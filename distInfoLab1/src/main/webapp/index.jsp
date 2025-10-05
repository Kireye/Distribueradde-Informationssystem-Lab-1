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

  <section class="grid">
    <!-- Produkt A -->
    <article class="card">
      <a href="${pageContext.request.contextPath}/item.jsp?id=A&name=AirPods%20Pro&price=2990&stock=12&category=Elektronik&image=airpods.jpg">
        <img src="${pageContext.request.contextPath}/images/airpods.jpg" alt="AirPods Pro" class="thumb">
      </a>
      <h3>
        <a href="${pageContext.request.contextPath}/item.jsp?id=A&name=AirPods%20Pro&price=2990&stock=12&category=Elektronik&image=airpods.jpg">AirPods Pro</a>
      </h3>
      <p class="price">2 990 kr</p>
      <p class="muted">I lager</p>
    </article>

    <!-- Produkt B -->
    <article class="card">
      <a href="${pageContext.request.contextPath}/item.jsp?id=B&name=Kaffemaskin%20Deluxe&price=1490&stock=5&category=Hem%20%26%20k%C3%B6k&image=espressomachine.jpg">
        <img src="${pageContext.request.contextPath}/images/espressomachine.jpg" alt="Kaffemaskin Deluxe" class="thumb">
      </a>
      <h3>
        <a href="${pageContext.request.contextPath}/item.jsp?id=B&name=Kaffemaskin%20Deluxe&price=1490&stock=5&category=Hem%20%26%20k%C3%B6k&image=espressomachine.jpg">Kaffemaskin Deluxe</a>
      </h3>
      <p class="price">1 490 kr</p>
      <p class="muted">Få kvar</p>
    </article>

    <!-- Produkt C -->
    <article class="card">
      <a href="${pageContext.request.contextPath}/item.jsp?id=C&name=Barnspel%20Zoo&price=249&stock=0&category=Leksaker%20%26%20spel&image=zoopanic.jpg">
        <img src="${pageContext.request.contextPath}/images/zoopanic.jpg" alt="Barnspel Zoo" class="thumb">
      </a>
      <h3>
        <a href="${pageContext.request.contextPath}/item.jsp?id=C&name=Barnspel%20Zoo&price=249&stock=0&category=Leksaker%20%26%20spel&image=zoopanic.jpg">Barnspel Zoo</a>
      </h3>
      <p class="price">249 kr</p>
      <p class="muted">Slut</p>
    </article>
  </section>
</main>

</body>
</html>