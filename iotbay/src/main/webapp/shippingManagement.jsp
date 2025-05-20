<%@ page import="java.util.List" %>
<%@ page import="model.ShippingManagement" %>
<%@ page import="java.time.LocalDate" %>
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
    <a href="shopping-cart.jsp">Return to cart</a>
</header>

<div class="container">
  <div class="main-container">
    <div class="centered-container">
      <h1>Shipping</h1>


<% 
  boolean hasShipments = shipments != null && !shipments.isEmpty();
%>
<% if (!hasShipments && edit == null) { %>
    <!-- CREATE form (blank inputs) -->
    <form action="ShippingController" method="post">
      <input type="hidden" name="action" value="create"/>
       <input type="hidden" name="orderId" value="<%= oid != null ? oid : "" %>"/>

      <div class="form-group">
        <label>Order ID:</label>
        <input type="text"
        value="<%= oid != null ? oid : "" %>" 
        disabled class="no-interact"/>
      </div>

      <div class="form-group">
        <label>Date:</label>
        <input type="date" name="shipmentDate" required 
        value="<%= LocalDate.now().toString() %>" 
        disabled class="no-interact"/>
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

    <% } else if (edit != null) { %>
    <!-- EDIT form -->
    <form action="ShippingController" method="post">
      <input type="hidden" name="action" value="update"/>
      <input type="hidden" name="shipmentId" value="<%= edit.getShipmentId() %>"/>

      <div class="form-group">
      <!-- This will submit order id after change -->
        <input 
          type="hidden" 
          name="orderId" 
          value="<%= edit.getOrderId() %>" />

      <!-- This one decorative and non-interactive -->
        <input 
          type="text" 
          value="<%= edit.getOrderId() %>" 
          disabled 
          class="readonly-field"/>
      </div>
      <div class="form-group">
        <label>Date:</label>
        <input type="date" name="shipmentDate"
               value="<%= edit.getShipmentDate() %>" disabled class="no-interact"/>
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



  

<!-- Update/Delete shipment -->
<% if (shipments != null && !shipments.isEmpty()) { %>
  <% for (ShippingManagement s : shipments) { %>
    <div class="card">
      <br><h3></strong>Shipment ID: </strong><%= s.getShipmentId() %></h3>
      <br><p></strong>Order ID:   </strong><%= s.getOrderId() %></p>
      <br><p></strong>Date:        </strong><%= s.getShipmentDate() %></p>
      <br><p></strong>Method:      </strong><%= s.getShippingMethod() %></p>
      <br><p></strong>Address:     </strong><%= s.getAddress() %></p><br>

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
      <% } %>
    <% } %>
  




      </form>
    </div>
  </div>
</div>
</body>
</html>

