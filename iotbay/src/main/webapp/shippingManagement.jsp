<%--
<%@ page import="java.util.List" %>
<%@ page import="model.ShippingManagement" %>
<%@ page import="model.Order" %>
<%@ page session="true" %>

<%
    List<ShippingManagement> shipments =
        (List<ShippingManagement>) request.getAttribute("shipments");

        Order order = (Order) request.getAttribute("order");
%>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Shipping Management</title>
  <link rel="stylesheet" href="css/subpages/shippingManagement.css">
</head>
<body>
<header>
    <div class="logo">
        <img src="assets/images/iotbay_logo.png" alt="IoTBay">
    </div>
    <a href="index.jsp">Return to Main Page</a>
</header>

<!-- Create a new shipment -->
<div class="container">
  <div class="main-container">
    <div class="centered-container">
      <form action="ShippingController" method="post">
       
       <div class="card create-form">
        <h2>Create New Shipment</h2>

        <div class="form-group">
          <<% if (order != null) { %>
          <!-- Hidden OrderID field -->
          <input type="hidden" name="orderId" value="<%= order.getOrderID() %>" />
          <p>Creating shipment for Order #<%= order.getOrderID() %></p>
          <% } else { %>
            <p style="color:red;">No order selected, cannot create shipment.</p>
          <% } %>
          </div>

        <div class="form-group">
          <label for="shipmentDate">Shipment Date:</label>
          <input type="date" name="shipmentDate" id="shipmentDate" required>
        </div>

        <div class="form-group">
          <label for="shippingMethod">Shipping Method:</label>
          <select name="shippingMethod" id="shippingMethod" required>
            <option value="">Select a shipping method</option>
            <option value="Standard">Standard</option>
            <option value="Express">Express</option>
          </select>
        </div>

        <div class="form-group">
          <label for="address">Address:</label>
          <input type="text" name="address" id="address" required>
        </div>

        <button type="submit" name="action" value="create">Create</button>
      </div> <br>
      </form>
      </div>
      </div>
      </div>

      <!-- Update/Delete shipment -->
      <form action="ShippingController" method="post">
      <div class="container">
        <div class="main-container">
          <div class="centered-container">
          <h2>Existing Shipments</h2>

        <% if (shipments == null || shipments.isEmpty()) { %>
          <p>No shipments found.</p>
        <% } else { %>
          <p>Created Shipment Details</p>

        <% for (ShippingManagement s : shipments) { %>
          <div class="card">
            <h3>Shipment ID: <%= s.getShipmentId() %></h3>
            <p>Order ID:    <%= s.getOrderId() %></p>
            <p>Date:        <%= s.getShipmentDate() %></p>
            <p>Method:      <%= s.getShippingMethod() %></p>
            <p>Address:     <%= s.getAddress() %></p>

            <input type="hidden" name="shipmentId"     value="<%= s.getShipmentId() %>"/>
            <input type="hidden" name="orderId"        value="<%= s.getOrderId() %>"/>
            <input type="hidden" name="shipmentDate"   value="<%= s.getShipmentDate() %>"/>
            <input type="hidden" name="shippingMethod" value="<%= s.getShippingMethod() %>"/>
            <input type="hidden" name="address"        value="<%= s.getAddress() %>"/>

            <button type="submit" name="action" value="update">Update</button>
            <button type="submit" name="action" value="delete">Delete</button>
          </div>
        <% } %>
        <% } %>
      </form>
    </div>
  </div>
</div>
</body>
</html> 
--%>

<%@ page import="java.util.List" %>
<%@ page import="model.ShippingManagement" %>
<%@ page import="model.Order" %>
<%@ page session="true" %>

<%
    List<ShippingManagement> shipments =
        (List<ShippingManagement>) request.getAttribute("shipments");
    ShippingManagement edit = 
        (ShippingManagement) request.getAttribute("updateShipment");
%>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Shipping Management</title>
  <link rel="stylesheet" href="css/subpages/shippingManagement.css">
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



<% if (edit == null) { %>
    <!-- CREATE form (blank inputs) -->
    <form action="ShippingController" method="post">
      <input type="hidden" name="action" value="create"/>
      <div class="form-group">
        <label>Order ID:</label>
        <input type="text" name="orderId" required/>
      </div>
      <div class="form-group">
        <label>Date:</label>
        <input type="date" name="shipmentDate" required/>
      </div>
      <div class="form-group">
        <label>Method:</label>
        <select name="shippingMethod" required>
          <option value="">Select</option>
          <option>Standard</option>
          <option>Express</option>
        </select>
      </div>
      <div class="form-group">
        <label>Address:</label>
        <input type="text" name="address" required/>
      </div>
      <button>Create</button>
    </form>
  <% } else { %>
    <!-- EDIT form (pre-populated from `edit`) -->
    <form action="ShippingController" method="post">
      <input type="hidden" name="action" value="update"/>
      <input type="hidden" name="shipmentId" value="<%= edit.getShipmentId() %>"/>

      <div class="form-group">
        <label>Order ID:</label>
        <input type="text" name="orderId"
               value="<%= edit.getOrderId() %>" readonly/>
      </div>
      <div class="form-group">
        <label>Date:</label>
        <input type="date" name="shipmentDate"
               value="<%= edit.getShipmentDate() %>" required/>
      </div>
      <div class="form-group">
        <label>Method:</label>
        <select name="shippingMethod" required>
          <option value="Standard"
            <%= "Standard".equals(edit.getShippingMethod())?"selected":"" %>>
            Standard
          </option>
          <option value="Express"
            <%= "Express".equals(edit.getShippingMethod())?"selected":"" %>>
            Express
          </option>
        </select>
      </div>
      <div class="form-group">
        <label>Address:</label>
        <input type="text" name="address"
               value="<%= edit.getAddress() %>" required/>
      </div>
      <button>Save Changes</button>
      <a href="ShippingController"><button> Cancel </button></a>
    </form>
  <% } %>
  </div>
  </div>
  </div>

<!-- Update/Delete shipment -->
  <div class="container">
  <div class="main-container">
    <div class="centered-container">
    <!-- Search form -->
<form action="ShippingController" method="get" class="search-form">
  <input type="hidden" name="action" value="search"/>

  <div class="form-group">
  <label for="shipmentIdSearch">Shipment ID:</label>
  <input
    type="number"
    name="shipmentId"
    id="shipmentIdSearch"
    placeholder="e.g. 42"
  />
  </div>

  <div class="form-group">
  <label for="dateSearch">Shipment Date:</label>
  <input 
    type="date"
    name="shipmentDate"
    id="dateSearch"
  />
  </div>


  <button type="submit">Search</button>
</form>
      
          <h2>Existing Shipments</h2>

        <% if (shipments == null || shipments.isEmpty()) { %>
  <p>No shipments found.</p>
<% } else { %>
  <% for (ShippingManagement s : shipments) { %>
    <div class="card">
      <h3>Shipment ID: <%= s.getShipmentId() %></h3>
      <p>Order ID:    <%= s.getOrderId() %></p>
      <p>Date:        <%= s.getShipmentDate() %></p>
      <p>Method:      <%= s.getShippingMethod() %></p>
      <p>Address:     <%= s.getAddress() %></p>

        <!-- EDIT: GET form with a submit button -->
      <form action="ShippingController" method="get" style="display:inline;">
        <input type="hidden" name="action" value="update"/>
        <input type="hidden" name="shipmentId" value="<%= s.getShipmentId() %>"/>
        <button type="submit" class="btn-primary">Update</button>
      </form>

      <!-- DELETE: POST form with a submit button -->
      <form action="ShippingController" method="post" style="display:inline;">
        <input type="hidden" name="action" value="delete"/>
        <input type="hidden" name="shipmentId" value="<%= s.getShipmentId() %>"/>
        <button type="submit" class="btn-primary">Delete</button>
      </form>
          </div>
  <% } %>  <!-- closes for -->
<% } %>    <!-- closes if/else -->

      </form>
    </div>
  </div>
</div>
</body>
</html>

