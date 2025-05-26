<%@ page import="java.util.List, model.ShippingManagement, model.users.User" %>
<%@ page import="java.util.List" %>
<%@ page session="true" %>
<%
    List<ShippingManagement> shipments = 
        (List<ShippingManagement>) request.getAttribute("shipments");
    ShippingManagement edit = 
        (ShippingManagement) request.getAttribute("updateShipment");
    User user = (User) session.getAttribute("user");

    String showAllParam = request.getParameter("showAll");
    String sidParam     = request.getParameter("shipmentId");
    String dateParam    = request.getParameter("shipmentDate");
    boolean hasSearched =
        (showAllParam != null)
     || (sidParam     != null && !sidParam.isEmpty())
     || (dateParam    != null && !dateParam.isEmpty());
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
      <h1>Shipment History</h1>
      <br/>

      <% if (edit != null) { %>
        <!-- EDIT FORM FOR ONE SHIPMENT -->
        <form action="${pageContext.request.contextPath}/ShippingController" method="post">
          <input type="hidden" name="action" value="update"/>
          <input type="hidden" name="shipmentId" value="<%= edit.getShipmentId() %>"/>

          <button type="submit">Save Changes</button>
          <a href="${pageContext.request.contextPath}/ShippingListController?showAll=1">
            <button type="button">Cancel</button>
          </a>
        </form>

      <% } else { %>
        <!-- SEARCH FORM -->
        <form action="${pageContext.request.contextPath}/ShippingListController"
              method="get" class="search-form">
          <div class="form-group">
            <label for="shipmentId">Shipment ID</label>
            <input type="number" name="shipmentId" id="shipmentId" placeholder="e.g. 42"/>
          </div>
          <div class="form-group">
            <label for="shipmentDate">Shipment Date</label>
            <input type="date" name="shipmentDate" id="shipmentDate"/>
          </div>
          <button type="submit">Search</button>
          <button type="submit" name="showAll" value="true">
            Show All
          </button>
        </form>
        <br/>

        <% if (hasSearched) { %>
          <% if (user == null) { %>
            <a href="${pageContext.request.contextPath}/login.jsp">
            <p>Only registered users can view shipment history.</p>
            </a>

          <% } else if (shipments == null || shipments.isEmpty()) { %>
            <p>No shipments found.</p>

          <% } else { %>
            <% for (ShippingManagement s : shipments) { %>
              <div class="card">
                <h3>Shipment #<%= s.getShipmentId() %></h3>
                <br><p><strong>Order ID:</strong>  <%= s.getOrderId() %></p>
                <br><p><strong>Date:</strong>      <%= s.getShipmentDate() %></p>
                <br><p><strong>Method:</strong>    <%= s.getShippingMethod() %></p>
                <br><p><strong>Address:</strong>   <%= s.getAddress() %></p>
                
                <!-- UPDATE -->
                <form action="${pageContext.request.contextPath}/ShippingController"
                      method="get" style="display:inline;">
                  <input type="hidden" name="action" value="update"/>
                  <input type="hidden" name="shipmentId"
                         value="<%= s.getShipmentId() %>"/>
                  <button type="submit">Update</button>
                </form>
                <!-- DELETE -->
                <form action="${pageContext.request.contextPath}/ShippingDeleteController"
                      method="post" style="display:inline;"
                      onsubmit="return confirm('Delete shipment #<%= s.getShipmentId() %>?');">
                  <input type="hidden" name="action" value="delete"/>
                  <input type="hidden" name="shipmentId"
                         value="<%= s.getShipmentId() %>"/>
                  <button type="submit">Delete</button>
                </form>
              </div>
              <br/>
            <% } %>
          <% } %>
        <% } %>
      <% } %>

    </div>
  </div>
</div>
</body>
</html>
