<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import= "model.ShippingManagement" %>
<%@ page import= "controllers.ShipmentController" %>
<%@ page import="model.Order" %>
<%@ page import="controllers.OrderController" %>

<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Shipping Management</title>
  <link rel="stylesheet" href="css/subpages/payment.css">
</head>
<body>
<header>
  <div class="logo">
    <img src="assets/images/iotbay_logo.png" alt="IoTBay">
  </div>
  <a href="index.jsp">Return to Main Page</a>
</header>


<div class="container">
  <div class="main-container">
    <div class="centered-container">
      <h1>Shipping Management</h1>
      <form action="ShipmentController" method="post">
        <label for="shippingMethod">Shipping Method:</label><br>
        <select name="shippingMethod" id="shippingMethod" required>
          <option value="Standard">Standard</option>
          <option value="Express">Express</option>
        </select><br><br>

        <label for="stName">Street name:</label><br>
        <input type="text" name="stName" id="stName" required><br>
        <label for="suburb">Suburb:</label><br>
        <input type="text" name="suburb" id="suburb" required><br>
        <label for="state">State:</label><br>
                <input type="text" name="state" id="state" required><br>
        <label for="Address">Address</label><br>
        <input type="text" name="Address" id="Address" required><br>

        <label for="Date">Date</label><br>
        <input type="Date" name="Date" id="Date" required><br>
        
        <h2>Shipments</h2>
                <% if (ShippingManagement.isEmpty()) { %>
                <p>No Shipment found.</p>
                <% } else { %>
                <p>Click on an Shipment to view its details.</p>
                <% } %>
                <% for (ShippingManagement shipmentManagement : ShippingManagement) { %>
                <div class="order-card">
                    <h3>Shipment ID: <%= shipmentManagement.getShipmentId() %>
                    </h3>
                    <p>Order ID: <%= order.getOrderDate() %>
                    </p>
                    <input type="hidden" name="ShipmentId" value="<%= order.getShipmentId() %>">
                    <input type="submit" value="View Shipment Details">
                </div>

        <input type="submit" value="Submit">
      </form>
    </div>
  </div>
</div>
</body>
</html>