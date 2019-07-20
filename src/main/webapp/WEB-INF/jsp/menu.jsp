<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: Munoo
  Date: 03.07.2019
  Time: 10:37
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<jsp:include page="../fragments/header.jsp"/>
<body>
    <jsp:include page="../fragments/bodyHeader.jsp"/>
    <h1>Menu</h1>
    <h2>Restaurant - ${restaurant.name}</h2>

    <table border="1" cellpadding="8" cellspacing="0">
        <thead>
            <tr>
                <td>Name</td>
                <td>Price</td>
                <td>Date</td>
                <td></td>
                <td></td>
            </tr>
        </thead>
        <tbody>
            <c:forEach items="${menu}" var="meal">
                <jsp:useBean id="meal" type="com.train4game.munoon.model.Meal" />
                <tr>
                    <td>${meal.name}</td>
                    <td>${meal.price}</td>
                    <td>${meal.date}</td>
                    <td><a href="${pageContext.request.contextPath}/menu/delete?deleteId=${meal.id}&id=${restaurant.id}">Delete</a></td>
                    <td><a href="${pageContext.request.contextPath}/menu/edit?editId=${meal.id}&id=${restaurant.id}">Edit</a></td>
                </tr>
            </c:forEach>
        </tbody>
    </table>

    <c:choose>
        <c:when test="${edit == null}">
            <form method="post" action="${pageContext.request.contextPath}/menu/create">
                <input type="hidden" name="restaurant" value="${restaurant.id}">
                <input type="text" name="name"><br>
                <input type="number" name="price"><br>
                <input type="datetime-local" name="date"><br>
                <input type="submit" value="Create">
            </form>
        </c:when>
        <c:otherwise>
            <form method="post" action="${pageContext.request.contextPath}/menu/update">
                <input type="hidden" name="mealId" value="${edit.id}">
                <input type="hidden" name="restaurant" value="${restaurant.id}">
                <input type="text" name="name" value="${edit.name}"><br>
                <input type="number" name="price" value="${edit.price}"><br>
                <input type="datetime-local" name="date" value="${edit.date}"><br>
                <input type="submit" value="Edit">
            </form>
        </c:otherwise>
    </c:choose>

    <jsp:include page="../fragments/footer.jsp"/>
</body>
</html>
