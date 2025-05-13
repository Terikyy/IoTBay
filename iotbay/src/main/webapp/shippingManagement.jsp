<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import= "model.ShippingManagement" %>
<%@ page import= "controllers.ShipmentController" %>
<%@ page import="model.users.User" %>
<%@ page import="controllers.UserController" %>
<%
  List<ShippingManagement> shipments =
      (List<ShippingManagement>) request.getAttribute("shipments");
  ShippingManagement editing =
      (ShippingManagement) request.getAttribute("editingShipment");
%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Shipping Management</title>
  <style>
    .box { border:1px solid #ccc; padding:10px; margin:10px; }
    .add-new { background:#eef; cursor:pointer; }
    #formContainer { display:none; position:fixed; top:20%; left:30%; 
      background:#fff; padding:20px; border:1px solid #666; }
  </style>
  <script>
    function openForm() {
      document.getElementById('formContainer').style.display = 'block';
    }
    function closeForm() {
      document.getElementById('formContainer').style.display = 'none';
    }
  </script>
</head>
<body>
  <h1>Shipping Management</h1>

  <!-- Search form -->
  <form method="get" action="shipment">
    <input type="hidden" name="action" value="search"/>
    <label>Shipment ID:
      <input type="number" name="shipmentId" value="" />
    </label>
    <label>Date:
      <input type="date" name="shipmentDate" value="" />
    </label>
    <button type="submit">Search</button>
    <button type="button" onclick="location.href='shippingManagement.jsp'">
      Clear
    </button>
  </form>

  <h2>Select a shipment</h2>
  <div>
    <%
      if (shipments != null && !shipments.isEmpty()) {
        for (ShippingManagement s : shipments) {
    %>
      <div class="box">
        <p><strong>Shipment #</strong> <%= s.getShipmentId() %></p>
        <p><strong>Order ID:</strong> <%= s.getOrderId() %></p>
        <p><strong>Date:</strong> <%= s.getShipmentDate() %></p>
        <p><strong>Method:</strong> <%= s.getDeliveryMethod() %></p>
        <p><strong>Address:</strong> <%= s.getAddress() %></p>
        <button onclick="location.href='shipment?action=edit&shipmentId=<%=s.getShipmentId()%>'">
          Edit
        </button>
        <form method="post" action="shipment" style="display:inline"
              onsubmit="return confirm('Delete this shipment?');">
          <input type="hidden" name="action" value="delete"/>
          <input type="hidden" name="shipmentId" value="<%=s.getShipmentId()%>"/>
          <button type="submit">Delete</button>
        </form>
      </div>
    <%
        }
      } else {
    %>
      <p>No shipments found.</p>
    <%
      }
    %>

    <!-- Add new button -->
    <div class="box add-new" onclick="openForm()">
      + Add New Shipment
    </div>
  </div>

  <!-- Create/Edit form -->
  <div id="formContainer">
    <h3><%= (editing != null) ? "Edit Shipment" : "New Shipment" %></h3>
    <form method="post" action="shipment">
      <input type="hidden" name="action"
             value="<%= (editing!=null) ? "update" : "create" %>"/>
      <% if (editing != null) { %>
        <input type="hidden" name="shipmentId"
               value="<%= editing.getShipmentId() %>"/>
      <% } %>

      <label>Order ID:
        <input type="number"
               name="orderId"
               value="<%= (editing != null) ? editing.getOrderId() : "" %>"
               required />
      </label><br/>
      
      <label>Date:
        <input type="date" name="shipmentDate"
               value="<%= (editing!=null)? editing.getShipmentDate(): "" %>"
               required />
      </label><br/>

      <label>Delivery Method:
        <select name="deliveryMethod" required>
          <option value="" disabled <%= (editing == null || editing.getDeliveryMethod().isEmpty()) ? "selected" : "" %>>Select a method</option>
          <option value="Standard" <%= (editing != null && "Standard".equals(editing.getDeliveryMethod())) ? "selected" : "" %>>Standard</option>
          <option value="Express" <%= (editing != null && "Express".equals(editing.getDeliveryMethod())) ? "selected" : "" %>>Express</option>
          <option value="Overnight" <%= (editing != null && "Overnight".equals(editing.getDeliveryMethod())) ? "selected" : "" %>>Overnight</option>
        </select>
            </label><br/>

      <label>Address:
        <input type="text" name="address"
               value="<%= (editing!=null)? editing.getAddress(): "" %>"
               required />
      </label><br/>


      <button type="submit">
        <%= (editing!=null)? "Update" : "Create" %>
      </button>
      <button type="button" onclick="closeForm()">Cancel</button>
    </form>
  </div>
</body>
</html>