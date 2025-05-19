<%@ page import="model.users.User" %>
<%@ page import="controllers.UserController" %>
<%@ page import="model.Payment" %>
<%@ page import="controllers.PaymentController" %>
<%@ page import="model.dao.PaymentDAO" %>
<%@ page session="true" %>
<%@ page import="model.Order" %>
<%@ page import="controllers.OrderController" %>
<%@ page import="java.sql.SQLException" %>


<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Payment</title>
    <link rel="stylesheet" href="css/subpages/payment.css">

    <%
        User user = (User) session.getAttribute("user");
        int orderId = (int) session.getAttribute("orderId");

        Order order = null;
        try {
            order = OrderController.getOrderById(orderId, request, response);
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
    <a href="index.jsp">Return to Order</a>

    <a href="${pageContext.request.contextPath}/ShippingController">Back to Shipment</a>

</header>
<div class="container">
    <div class="main-container">
        <div class="centered-container">
            <h2>Payment</h2>
            <div class="totalPrice">Total Amount: $<%= String.format("%.2f", order.totalPrice) %>
            </div>
            <form action="PayPalServlet" method="post">
                <label for="paymentMethod">Payment Method:</label><br>
                <select name="paymentMethod" id="paymentMethod" required onchange="this.form.submit()">
                    <option value="credit-card" selected>Credit Card</option>
                    <option value="paypal">PayPal</option>
                </select><br><br>
            </form>
            <form action="PaymentController" method="post">
                <div id="cardDetails">
                    <label for="nameOnCard">Name on Card:</label><br>
                    <input type="text" name="nameOnCard" id="nameOnCard" required><br>
                    <label for="cardNumber">Card Number:</label><br>
                    <input type="number" name="cardNumber" id="cardNumber" required><br>
                    <label for="expiryDate">Expiry Date:</label><br>
                    <input type="date" name="expiryDate" id="expiryDate" required><br>
                    <label for="cvv">CVV:</label><br>
                    <input type="number" name="cvv" id="cvv" required><br>
                </div>


                <input type="submit" value="Pay Now">
            </form>
        </div>
    </div>
</div>
</body>
</html>