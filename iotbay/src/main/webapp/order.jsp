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
                <input type="text" name="name" id="name" required><br>
                <label for="email">Email:</label><br>
                <input type="email" name="email" id="email" required><br>
                <label for="stNum">Street Number:</label><br>
                <input type="numbers" name="stNum" id="stNum" required title="Please enter a Street Number">><br>
                <label for="stName">Street Name:</label><br>
                <input type="text" name="stName" id="stName" required><br>
                <label for="country">Country:</label><br>
                <input type="text" name="country" id="country" required><br>
                <label for="state">State:</label><br>
                <input type="text" name="state" id="state" required><br>
                <label for="suburb">Suburb:</label><br>
                <input type="text" name="suburb" id="suburb" required><br>
                <label for="city">City:</label><br>
                <input type="text" name="city" id="city" required><br>
                <label for="zip">Zip Code:</label><br>
                <input type="numbers" name="zip" id="zip" pattern="\d{4}" maxlength="4" required title="Please enter a 4-digit zip code"><br>
                <label for="phone">Phone Number:</label><br>
                <input type="text" name="phone" id="phone" required><br>
                <input type="submit" value="Order">
            </form>
        </div>
    </div>
</div>
</body>
</html>