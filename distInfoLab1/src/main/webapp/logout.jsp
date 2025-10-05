<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.werkstrom.distinfolab1.bo.facades.UserFacade" %>

<%
    try {
        UserFacade.logout();
        if (session != null) {
            session.invalidate();
        }
        response.sendRedirect(request.getContextPath() + "/index.jsp");
        return;
    } catch (Exception e) {
%>
        <h2>Logout failed</h2>
        <pre><%= e.getMessage() %></pre>
<%
    }
%>