<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: Munoo
  Date: 03.07.2019
  Time: 11:18
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Vote</title>
</head>
<body>
    <h2><a href="../..">Main Page</a></h2>
    <h1>Vote</h1>

    <table border="1" cellpadding="8" cellspacing="0">
        <thead>
            <td>User</td>
            <td>Restaurant</td>
            <td>Date</td>
            <td></td>
            <td></td>
        </thead>
        <tbody>
            <c:forEach var="vote" items="${votes}">
                <jsp:useBean id="vote" class="com.train4game.munoon.model.Vote" />
                <tr>
                    <td>${vote.user}</td>
                    <td>${vote.restaurant}</td>
                    <td>${vote.date}</td>
                    <td><a href="${pageContext.request.contextPath}/vote/edit?id=${vote.id}">Edit</a></td>
                    <td><a href="${pageContext.request.contextPath}/vote/delete?id=${vote.id}">Delete</a></td>
                </tr>
            </c:forEach>
        </tbody>
    </table>

    <div>
        <h2>${edit == null ? "Create new vote" : "Edit vote"}</h2>
        <form method="post" action="${pageContext.request.contextPath}/vote/${edit == null ? "create" : "update"}">
            <c:if test="${edit != null}">
                <input type="hidden" name="editId" value="${edit.id}">
            </c:if>
            <select name="restaurantId">
                <c:forEach var="option" items="${restaurants}">
                    <jsp:useBean id="option" class="com.train4game.munoon.model.Restaurant" />
                    <option value="${option.id}">${option.name}</option>
                </c:forEach>
            </select>
            <input type="submit" value="${edit == null ? "Create" : "Edit"}">
        </form>
    </div>
</body>
</html>
