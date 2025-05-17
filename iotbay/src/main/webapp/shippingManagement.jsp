<%@ page import="java.util.List" %>
<%@ page import="model.ShippingManagement" %>
<%@ page import="model.Order" %>
<%@ page session="true" %>

<%
    List<ShippingManagement> shipments =
        (List<ShippingManagement>) request.getAttribute("shipments");
    ShippingManagement edit = 
        (ShippingManagement) request.getAttribute("updateShipment");
    Integer oid = (Integer) session.getAttribute("orderId");
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
       <input type="hidden" name="orderId" value="<%= oid != null ? oid : "" %>"/>

    <div class="form-group">
      <label>Order ID:</label>
      <input type="text"
             value="<%= oid != null ? oid : "" %>"
             readonly class="readonly-field"/>
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

