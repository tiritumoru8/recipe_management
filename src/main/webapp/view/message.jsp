<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <%
 	String errorMsg = (String) request.getAttribute("errorMsg");
 	String message = (String) request.getAttribute("message");
 	if(errorMsg != null) {
 %>
 	<p class="error"><%=errorMsg %></p><br><br>
 <%
 	}else if(message != null) {
 %>
 	<p><%=message %></p><br><br>
 <%
 	}
 %>
