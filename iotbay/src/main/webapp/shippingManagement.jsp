<%@ page import="java.util.List" %>
<%@ page import="model.ShippingManagement" %>
<%@ page import="java.time.LocalDate" %>
<%@ page import="model.Order" %>
<%@ page import="model.users.User" %>
<%@ page import="model.Product" %>
<%@ page session="true" %>

<%
    List<ShippingManagement> shipments =
        (List<ShippingManagement>) request.getAttribute("shipments");
    ShippingManagement edit = 
        (ShippingManagement) request.getAttribute("updateShipment");

    Integer oid = (Integer) session.getAttribute("orderId");
    User user = (User) session.getAttribute("user");

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
          <option>Local Delivery/Pick up</option>
        </select>
      </div>
      <div class="form-group">
        <label>Address:</label>
        <input type="text" name="address" required/>
      </div>
      
      <!-- Error message display -->
      <% String errorMessage = (String) request.getAttribute("errorMessage"); %>
      <% if (errorMessage != null) { %>
          <strong><a href = "confirmation.jsp"> <%= errorMessage %> </a></strong>
      <% } %>

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
          <option value="Local Delivery/Pick up"
            <%= "Local Delivery/Pick up".equals(edit.getShippingMethod())?"selected":"" %>>
            Local Delivery/Pick up
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
  


      </form>
    </div>
  </div>
</div>
</body>
</html>

