<%--
  Created by IntelliJ IDEA.
  User: timothykugler
  Date: 19.05.25
  Time: 20:05
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="model.users.User" %>
<%@ page session="true" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<% User user = (User) session.getAttribute("user"); %>
<html>
<head>
    <title>Title</title>
    <link rel="stylesheet" href="css/subpages/update-order.css">
</head>
<body>
<header>
    <div class="logo">
        <a href="${pageContext.request.contextPath}/products/list">
            <img src="${pageContext.request.contextPath}/assets/images/iotbay_logo.png" alt="IoTBay">
        </a>
    </div>
    <div class="header-right">
        <div class="nav-icons">
            <a href="${pageContext.request.contextPath}/account.jsp" title="Account" class="account-icon">
                <img src="${pageContext.request.contextPath}/assets/images/account_icon.png" alt="Account">
                <% if (user != null) { %>
                <span class="login-indicator"></span>
                <% } %>
            </a>
        </div>
        <div class="nav-icons">
            <a href="${pageContext.request.contextPath}/navigation.jsp" title="Navigation">
                <img src="${pageContext.request.contextPath}/assets/images/navigation_icon.png" alt="Navigation">
            </a>
        </div>
    </div>
</header>
<div class="container">
    <div class="main-container">
        <div class="centered-container">
            <h1>Update Order</h1>
            <form action="OrderUpdateController" method="post">
                <input type="hidden" name="orderID" value="<%= request.getParameter("orderID") %>">
                <label for="orderStatus">Order Status:</label>
                <select name="orderStatus" id="orderStatus">
                    <option value="Pending">Pending</option>
                    <option value="Shipped">Shipped</option>
                    <option value="Delivered">Delivered</option>
                    <option value="Cancelled">Cancelled</option>
                </select>
                <input type="submit" value="Update Order">
            </form>
        </div>
    </div>
</div>
</body>
</html>
