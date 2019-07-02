<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%--
  Created by IntelliJ IDEA.
  User: Munoo
  Date: 02.07.2019
  Time: 13:00
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>User</title>
</head>
<body>
    <h1>User Page</h1>
    <h2>Name - ${user.name}</h2>
    <h2>Email - ${user.email}</h2>
    <h2>Registered date - ${user.registered}</h2>
    <h2>Role - ${user.roles}</h2>

    <form method="post">
        <select name="userId">
            <option value="100">Nikita (Admin)</option>
            <option value="101">Vasya (User)</option>
            <option value="102">Petr (User)</option>
        </select>
        <input type="submit" value="Change user">
    </form>
</body>
</html>
