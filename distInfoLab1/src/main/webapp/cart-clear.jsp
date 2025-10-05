<%@ include file="/WEB-INF/jspf/db-init.jspf" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.werkstrom.distinfolab1.ui.UserInfo" %>
<%@ page import="com.werkstrom.distinfolab1.bo.facades.UserFacade" %>
<%@ page import="java.net.URLEncoder" %>
<%
  if (!"POST".equalsIgnoreCase(request.getMethod())) {
      response.sendRedirect(request.getContextPath() + "/cart.jsp");
      return;
  }

  UserInfo u = (session != null && session.getAttribute("user") instanceof UserInfo)
          ? (UserInfo) session.getAttribute("user") : null;

  if (u == null) {
      response.sendRedirect(request.getContextPath() + "/login.jsp");
      return;
  }

  try {
      UserFacade.emptyCart(u.getId());
      response.sendRedirect(request.getContextPath() + "/cart.jsp");
      return;
  } catch (Exception e) {
      String err = URLEncoder.encode("Failed to clear cart: " + e.getMessage(), "UTF-8");
      response.sendRedirect(request.getContextPath() + "/cart.jsp?error=" + err);
      return;
  }
%>