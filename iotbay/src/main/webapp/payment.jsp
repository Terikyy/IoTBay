<%@ page import="model.users.User" %>
<%@ page import="controllers.UserController" %>
<%@ page import="model.Payment" %>
<%@ page import="controllers.PaymentController" %>
<%@ page import="model.dao.PaymentDAO" %>
<%@ page session="true" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Payment</title>
    <link rel="stylesheet" href="css/subpages/payment.css">
</head>
<body>
<header>
    <div class="logo">
        <img src="assets/images/iotbay_logo.png" alt="IoTBay">
    </div>
    <a href="index.jsp">Return to Order</a> 
</header>
<div class="container">
    <div class="main-container">
        <div class="centered-container">
            <form action="PaymentController" method="post">
                <h2>Payment</h2>
                <label for="cardNumber">Card Number:</label><br>
                <input type="text" name="cardNumber" id="cardNumber" required><br>
                <label for="expiryDate">Expiry Date:</label><br>
                <input type="text" name="expiryDate" id="expiryDate" required><br>
                <label for="cvv">CVV:</label><br>
                <input type="text" name="cvv" id="cvv" required><br>
                <input type="submit" value="Pay">
            </form>
        </div>
    </div>
</div>
</body>
</html>