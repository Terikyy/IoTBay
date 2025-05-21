<%@ page import="java.util.List, model.ShippingManagement" %>
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
  <title>Shipping List</title>
  <link rel="stylesheet" href="css/subpages/shippingManagement.css">
</head>
<body>

<header>
  <div class="logo">
    <img src="assets/images/iotbay_logo.png" alt="IoTBay">
  </div>
  <a href="index.jsp" title="Main Page">Back to Main Page</a>
</header>

<div class="container">
  <div class="main-container">
    <div class="centered-container">
      <h1>Shipment History</h1>
      <br/>

      <% if (edit != null) { %>
        <!-- EDIT FORM FOR ONE SHIPMENT -->
        <form action="${pageContext.request.contextPath}/ShippingController" method="post">
          <input type="hidden" name="action" value="update"/>
          <input type="hidden" name="shipmentId" value="<%= edit.getShipmentId() %>"/>

          <div class="form-group">
            <label>Order ID:</label>
            <input type="text"
                   value="<%= edit.getOrderId() %>"
                   disabled
                   class="readonly-field"/>
            <input type="hidden"
                   name="orderId"
                   value="<%= edit.getOrderId() %>"/>
          </div>

          <div class="form-group">
            <label>Date:</label>
            <input type="date"
                   name="shipmentDate"
                   value="<%= edit.getShipmentDate() %>"
                   disabled
                   class="no-interact"/>
          </div>

          <div class="form-group">
            <label>Method:</label>
            <select name="shippingMethod" required>
              <option value="Standard"
                <%= "Standard".equals(edit.getShippingMethod()) ? "selected" : "" %>>
                Standard
              </option>
              <option value="Express"
                <%= "Express".equals(edit.getShippingMethod()) ? "selected" : "" %>>
                Express
              </option>
            </select>
          </div>

          <div class="form-group">
            <label>Address:</label>
            <input type="text"
                   name="address"
                   value="<%= edit.getAddress() %>"
                   required/>
          </div>

          <button type="submit" class="btn btn-primary">Save Changes</button>
          <a href="${pageContext.request.contextPath}/ShippingListController?showAll=1">
            <button type="button" class="btn btn-secondary">Cancel</button>
          </a>
        </form>

      <% } else { %>
        <!-- Search  -->
        <form action="${pageContext.request.contextPath}/ShippingListController"
              method="get"
              class="search-form">
          <div class="form-group">
            <label for="shipmentId">Shipment ID</label>
            <input type="number" name="shipmentId" id="shipmentId" placeholder="e.g. 42"/>
          </div>
          <div class="form-group">
            <label for="shipmentDate">Shipment Date</label>
            <input type="date" name="shipmentDate" id="shipmentDate"/>
          </div>
          <button type="submit" class="btn btn-sm btn-primary">Search</button>
          <button type="submit"
                  name="showAll" value="true"
                  class="btn btn-sm btn-secondary">
            Show All
          </button>
        </form>
        <br/>

        <!-- Shipping List -->
        <% if (shipments == null || shipments.isEmpty()) { %>
          <p>No shipments found.</p>
        <% } else { %>
          <% for (ShippingManagement s : shipments) { %>
            <div class="card">
              <h3>Shipment #<%= s.getShipmentId() %></h3>
              <p><strong>Order ID:</strong>  <%= s.getOrderId() %></p>
              <p><strong>Date:</strong>      <%= s.getShipmentDate() %></p>
              <p><strong>Method:</strong>    <%= s.getShippingMethod() %></p>
              <p><strong>Address:</strong>   <%= s.getAddress() %></p>
              <br/>

              <!-- UPDATE -->
              <form action="${pageContext.request.contextPath}/ShippingController"
                    method="get" style="display:inline;">
                <input type="hidden" name="action" value="update"/>
                <input type="hidden" name="shipmentId"
                       value="<%= s.getShipmentId() %>"/>
                <button type="submit" class="btn btn-sm btn-primary">
                  Update
                </button>
              </form>

              <!-- DELETE -->
              <form action="${pageContext.request.contextPath}/ShippingDeleteController"
                    method="post" style="display:inline;"
                    onsubmit="return confirm('Delete shipment #<%= s.getShipmentId() %>?');">
                <input type="hidden" name="action" value="delete"/>
                <input type="hidden" name="shipmentId"
                       value="<%= s.getShipmentId() %>"/>
                <button type="submit" class="btn btn-sm btn-danger">
                  Delete
                </button>
              </form>
            </div>
            <br/>
          <% } %>
        <% } %>
      <% } %>

    </div>
  </div>
</div>

</body>
</html>
