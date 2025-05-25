<%@ page import="java.util.List" %>
<%@ page import="model.Payment" %>
<%@ page session="true" %>
<%@ page import="java.util.Map" %>
<%@ page import="model.Product" %>
<%@ page import="model.users.User" %>

<%
    User user = (User) session.getAttribute("user");
    
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
        <a href="${pageContext.request.contextPath}/products/list">
            <img src="${pageContext.request.contextPath}/assets/images/iotbay_logo.png" alt="IoTBay">
        </a>
    </div>
    <div class="header-right">
        <div class="nav-icons" title="Shopping Cart">
            <a href="${pageContext.request.contextPath}/cart" class="cart-button">
                <img src="${pageContext.request.contextPath}/assets/images/cart_icon.png" alt="Shopping Cart">
                <%
                    List<Map<String, Object>> cartItems = (List<Map<String, Object>>) request.getAttribute("cartItems");
                    int itemCount = 0;
                    if (cartItems != null) {
                        for (Map<String, Object> item : cartItems) {
                            itemCount += (int) item.get("quantity");
                        }
                    }
                %>
                <span class="cart-count <%= itemCount > 0 ? "" : "hidden" %>">
                        <%= itemCount %>
                    </span>
            </a>
        </div>
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