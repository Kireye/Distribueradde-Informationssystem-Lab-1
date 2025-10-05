<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.werkstrom.distinfolab1.bo.facades.UserFacade" %>

<%
    try {
        // 1) Kör din facade-metod (statisk)
        UserFacade.logout();

        // 2) Stäng användarsessionen (om du vill logga ut både i DB/connection och i session)
        if (session != null) {
            session.invalidate();
        }

        // 3) Redirect (PRG-pattern så inte åtgärden råkar köras igen på refresh)
        response.sendRedirect(request.getContextPath() + "/index.jsp");
        return;
    } catch (Exception e) {
        // Minimal felhantering – visa något och låt sidan återgå
%>
        <h2>Utloggning misslyckades</h2>
        <pre><%= e.getMessage() %></pre>
<%
    }
%>