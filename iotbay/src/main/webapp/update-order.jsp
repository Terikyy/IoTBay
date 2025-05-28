<%@ page import="model.users.User" %>
<%@ page import="controllers.UserController" %>
<%@ page import="model.Order" %>
<%@ page import="controllers.OrderController" %>
<%@ page import="model.OrderItems" %>
<%@ page import="controllers.OrderItemServlet" %>
<%@ page import="model.Product" %>
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
        Order order = (Order) session.getAttribute("order");
        if (order == null) {
            // no order in session ⇒ redirect or show error
            response.sendRedirect("index.jsp");
            return;
        }

        if (user == null) {
            response.sendRedirect("index.jsp"); 
            return; // Important to stop JSP processing after redirect
        }
        List<OrderItem> orderItems = null;
        try {
            orderItems = OrderItemServlet.getOrderItems(order.getOrderID(), session);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    %>
</head>
<body>
<header>
    <div class="logo">
        <img src="assets/images/iotbay_logo.png" alt="IoTBay">
    </div>
    <a href="index.jsp" title="Main Page">Return to Home Page</a>
</header>
<div class="container">
    <div class="main-container">
        <div class="centered-container">
            <h2>Order items</h2>
            <% if (orderItems.isEmpty()) { %>
            <p>No order items found.</p>
            <% } else { %>
            <p>Use Delete button to remove items from the order.</p>
            <% } %>
            <% for (OrderItem orderItem : orderItems) { %>
            <div class="order-item-card">
                <h3>Item Name: <br> <%= OrderItemServlet.getProductById(orderItem.getProductID()).getName() %>
                </h3>
                <p>Quantity: <br> <%= orderItem.getQuantity() %>
                </p>
                <p>Price: <br> <%= orderItem.getPriceOnOrder() %>
>
                </form>
                <form method="get" action="OrderItemServlet">
                    <input type="hidden" name="action" value="delete">
                    <input type="hidden" name="productId" value="<%= orderItem.getProductID() %>">
                    <input type="hidden" name="orderId" value="<%= orderItem.getOrderID() %>">
                    <button type="submit" <%= !"PENDING".equals(order.getOrderStatus()) ? "disabled" : "" %>>
                        Delete
                    </button>
                </form>
            </div>
            <% } %>
        </div>
    </div>
</div>
</body>
</html>