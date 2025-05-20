<%@ page import="java.util.List" %>
<%@ page import="model.ShippingManagement" %>
<%@ page session="true" %>
<%
    List<ShippingManagement> shipments =
        (List<ShippingManagement>) request.getAttribute("shipments");
%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Shipping List</title>
  <link rel="stylesheet" href="css/subpages/shippingManagement.css">
</head>
<body>

<header>
    <div class="logo">
        <img src="assets/images/iotbay_logo.png" alt="IoTBay">
    </div>
    <a href="index.jsp">Back to Main Page</a>
</header>

  
  <div class="container">
    <div class="main-container">
        <div class="centered-container">
    <h1>Shipment History</h1><br>
    
  

  <!-- Search form -->
  <form action="${pageContext.request.contextPath}/ShippingListController" method="get" class="search-form">
    <div class="form-group">
      <label for="shipmentId">Shipment ID</label>
      <input type="number" name="shipmentId" id="shipmentId" placeholder="e.g. 42"/>
    </div>

    <div class="form-group">
      <label for="shipmentDate">Shipment Date</label>
      <input type="date" name="shipmentDate" id="shipmentDate" />
    </div>

    <button type="submit">Search</button>
    <button type="submit" name="showAll" value="true">Show All</button>

  </form>

  <!-- Shipping List -->
    <% if (shipments == null || shipments.isEmpty()) { %>
      <br><p>No shipments found.</p>
    <% } else { %>
      <% for (ShippingManagement s : shipments) { %>
        <div class="card">
          <br><h3>Shipment #<%= s.getShipmentId() %></h3>
          <p><strong>Order ID:</strong> <%= s.getOrderId() %></p>
          <p><strong>Date:</strong> <%= s.getShipmentDate() %></p>
          <p><strong>Method:</strong> <%= s.getShippingMethod() %></p>
          <p><strong>Address:</strong> <%= s.getAddress() %></p><br>
        </div>
      <% } %>
    <% } %>
  

        </div>
    </div>
</div>
</body>
</html>
