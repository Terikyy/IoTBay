<%--
  Created by IntelliJ IDEA.
  User: timothykugler
  Date: 19.05.25
  Time: 20:05
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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
        <div class="account">
            <a href="${pageContext.request.contextPath}/account.jsp">
                <img src="${pageContext.request.contextPath}/assets/images/account_icon.png" alt="Account">
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
