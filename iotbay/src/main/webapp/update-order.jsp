<%@ page import="model.users.User" %>
<%@ page import="controllers.UserController" %>
<%@ page import="model.Order" %>
<%@ page import="controllers.OrderController" %>
<%@ page import="model.lineproducts.OrderItem" %>
<%@ page import="controllers.OrderUpdateServlet" %>
<%@ page import="model.Product" %>
<%@ page import="java.util.List" %>
<%@ page import="java.sql.SQLException" %>
<%@ page session="true" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Order Items</title>
    <link rel="stylesheet" href="css/subpages/order.css">
    <%
        User user = (User) session.getAttribute("user");
        Order order = (Order) session.getAttribute("order");

        System.out.println("Opening order items for order: " + order.getOrderID());

        if (user == null) {
            response.sendRedirect("index.jsp"); 
            return; // Important to stop JSP processing after redirect
        }
        List<OrderItem> orderItems = null;
        try {
            orderItems = OrderUpdateServlet.getOrderItems(order.getOrderID(), session);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    %>
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
            <h2>Order items</h2>
            <% if (orderItems.isEmpty()) { %>
            <p>No order items found.</p>
            <% } else { %>
            <p>Use Delete button to remove items from the order.</p>
            <% } %>
            <% for (OrderItem orderItem : orderItems) { %>
            <div class="order-card">
                <h3>Item Name: <br> <%= OrderUpdateServlet.getProductById(orderItem.getProductID(), session).getName() %>
                </h3>
                <p>Quantity: <br> <%= orderItem.getQuantity() %>
                </p>
                <p>Price: <br> <%= orderItem.getPriceOnOrder() %>
                </p>

                <form method="post" action="OrderDeletionServlet">
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