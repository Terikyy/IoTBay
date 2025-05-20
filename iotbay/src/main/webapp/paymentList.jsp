
<%@ page import="java.util.List" %>
<%@ page import="model.Payment" %>
<%@ page session="true" %>

<%
    @SuppressWarnings("unchecked")
    List<Payment> payments = (List<Payment>) request.getAttribute("payments");
%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Payment History</title>
  <link rel="stylesheet" href="css/subpages/payment.css">
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
      <h1>Payment History</h1><br>

      <!-- Search form -->
      <form action="${pageContext.request.contextPath}/PaymentListController" method="get">
        <div class="form-group">
          <label for="paymentId">Payment ID</label>
          <input type="number" name="paymentId" id="paymentId" placeholder="e.g. 42"/>
        </div>

        <div class="form-group">
          <label for="paymentDate">Payment Date</label>
          <input type="date" name="paymentDate" id="paymentDate"/>
        </div>

        <button type="submit">Search</button>
        <button type="submit" name="showAll" value="1">Show All</button>
      </form>

      <!-- Payment List -->
      <% if (payments == null || payments.isEmpty()) { %>
        <br><p>No payments found.</p>
      <% } else { 
           for (Payment p : payments) { %>
        <div class="card">
          <h3>Payment #<%= p.getPaymentID() %></h3>
          <p><strong>Order ID:</strong>    <%= p.getOrderID() %></p>
          <p><strong>Amount:</strong>      $<%= String.format("%.2f", p.getAmountPaid()) %></p>
          <p><strong>Date:</strong>        <%= p.getPaymentDate() %></p>
          <p><strong>Status:</strong>      <%= p.getPaymentStatus() %></p>
        </div>
      <%   }
         } %>

    </div>
  </div>
</div>
</body>
</html>