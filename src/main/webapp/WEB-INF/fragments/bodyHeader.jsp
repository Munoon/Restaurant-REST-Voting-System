<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<div class="indent-from-a">
    <a href="${pageContext.request.contextPath}">Main page</a>
    <a href="${pageContext.request.contextPath}/user">Users page</a>
    <a href="${pageContext.request.contextPath}/restaurants">Restaurants page</a>
    <a href="${pageContext.request.contextPath}/vote">Votes page</a>
</div>
<hr>