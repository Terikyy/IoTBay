<%@ page import="model.users.User" %>
<%@ page import="controllers.UserController" %>
<%@ page import="model.Order" %>
<%@ page import="controllers.OrderController" %>
<%@ page import="model.dao.OrderDAO" %>
<%@ page import="java.util.List" %>
<%@ page import="java.sql.SQLException" %>
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
        if (user == null) {
            response.sendRedirect("index.jsp");
            return; // Important to stop JSP processing after redirect
        }
        List<Order> orders = null;
        try {
            orders = OrderController.getUserOrders(user.getUserID(), session);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if (orders == null) {
            response.sendRedirect("index.jsp");
            return; // Important to stop JSP processing after redirect
        }
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
                <h2>Orders</h2>
                <% if (orders.isEmpty()) { %>
                <p>No orders found.</p>
                <% } else { %>
                <p>Click on an order to view its details.</p>
                <% } %>
                <% for (Order order : orders) { %>
                <div class="order-card">
                    <h3>Order ID: <%= order.getOrderID() %>
                    </h3>
                    <p>Order Date: <%= order.getOrderDate() %>
                    </p>
                    <p>Order Status: <%= order.getOrderStatus() %>
                    </p>
                    <input type="hidden" name="orderId" value="<%= order.getOrderID() %>">
                    <input type="submit" value="View Order Details">
                </div>
                <% } %>
            </form>
        </div>
    </div>
</div>
</body>
</html>