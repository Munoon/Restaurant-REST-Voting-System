<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: Munoo
  Date: 02.07.2019
  Time: 13:24
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Restaurants</title>
</head>
<body>
    <h2><a href="../..">Main Page</a></h2>
    <h1>Restaurants</h1>
    <table border="1" cellpadding="8" cellspacing="0">
        <thead>
            <tr>
                <td>Name</td>
                <td></td>
                <td></td>
                <td></td>
            </tr>
        </thead>
        <tbody>
            <c:forEach items="${restaurants}" var="restaurant">
                <jsp:useBean id="restaurant" type="com.train4game.munoon.model.Restaurant" />
                <tr>
                    <td>${restaurant.name}</td>
                    <td><a href="./menu?id=${restaurant.id}">Menu</a></td>
                    <td><a href="${pageContext.request.contextPath}/restaurants/delete?id=${restaurant.id}">Delete</a></td>
                    <td><a href="${pageContext.request.contextPath}/restaurants/update?id=${restaurant.id}">Update</a></td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
    <c:if test="${edit != null}">
        <form method="post" action="${pageContext.request.contextPath}/restaurants/edit">
            <input type="hidden" name="type" value="edit">
            <input type="hidden" name="id" value="${edit.id}">
            <input type="text" name="name" value="${edit.name}">
            <input type="submit" value="Change">
        </form>
    </c:if>
    <c:if test="${edit == null}">
        <form method="post" action="${pageContext.request.contextPath}/restaurants/create">
            <input type="hidden" name="type" value="create">
            <input type="text" name="name">
            <input type="submit" value="Create">
        </form>
    </c:if>
</body>
</html>
