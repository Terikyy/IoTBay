<%@ page import="model.users.User" %>
<%@ page import="controllers.UserController" %>
<%@ page import="model.Order" %>
<%@ page import="controllers.OrderController" %>
<%@ page import="model.dao.OrderDAO" %>
<%@ page session="true" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Order</title>
    <link rel="stylesheet" href="css/subpages/order.css">
    <%
        User user = (User) session.getAttribute("user");
    %>
</head>
<body>
<header>
    <div class="logo">
        <img src="assets/images/iotbay_logo.png" alt="IoTBay">
    </div>
    <a href="index.jsp">Cancel Order</a>
</header>
<div class="container">
    <div class="main-container">
        <div class="centered-container">
            <form action="OrderController" method="post">
                <h2>Order</h2>
                <label for="name">Name:</label><br>
                <input type="text" name="name" id="name" value="<%=user.getName()%>" required><br>
                <label for="email">Email:</label><br>
                <input type="email" name="email" id="email" value="<%=user.getEmail()%>" required><br>

                <!-- TODO: Add orderItem stuff -->

                <input type="submit" value="Order">
            </form>
        </div>
    </div>
</div>
</body>
</html>