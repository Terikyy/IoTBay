<%@ page import="java.util.List, java.util.ArrayList" %>
<%@ page import="model.Payment, model.users.User" %>

<%
    // Retrieve the logged-in user from the session.
    User user = (User) session.getAttribute("user");
  
    List<Payment> payments = (List<Payment>) request.getAttribute("payments");
    if (payments == null) {
      payments = new ArrayList<>();
    }

    // Determine if a search was performed.
    Boolean hasSearched = (Boolean) request.getAttribute("hasSearched");
    if (hasSearched == null) { 
    hasSearched = false;
    } 
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
      <div class="payment-search">      
        <form action="${pageContext.request.contextPath}/PaymentListController" method="get">
          <div class="form-group">
            <label for="paymentId">Payment ID</label>
            <input type="number" name="paymentId" id="paymentId" placeholder="e.g. 42"/>
          </div>

          <div class="form-group">
            <label for="paymentDate">Payment Date</label>
            <input type="date" name="paymentDate" id="paymentDate"/>
          </div>
          
          <div class="button-group">
            <button type="submit" <%= (user == null ? "disabled" : "") %> >Search</button>
            <button type="submit" name="showAll" value="1" <%= (user == null ? "disabled" : "") %> >Show All</button>
          </div>
        </form>

        <%-- 1) “No such payment” when filters supplied but nothing matched --%>
        <% if (request.getAttribute("error") != null) { %>
          <p class="message error-message"><%= request.getAttribute("error") %></p>

        <%-- 2) “No payments exist” when Show All clicked and list is empty --%>
        <% } else if (request.getParameter("showAll") != null && payments.isEmpty()) { %>
          <p class="message">No payments exist.</p>
        <% } %>

        <% if (user == null) { %>
          <a href="${pageContext.request.contextPath}/login.jsp">
            <p class="message">You must be logged in to view your payment history.</p>
          </a>
        <% } 
        %>

      </div>

      <!-- Payment List -->
      <div class="card-container">
        <% if (user != null && !payments.isEmpty()) { 
              for (Payment p : payments) { %>
          <div class="card">
            <h3>Payment ID: #<%= p.getPaymentID() %></h3>
            <p>Order ID:   <%= p.getOrderID() %></p>
            <p>Amount:     $<%= String.format("%.2f", p.getAmountPaid()) %></p>
            <p>Date:       <%= p.getPaymentDate() %></p>
            <p>Status:     <%= p.getPaymentStatus() %></p>

            <!-- UPDATE button -->
            <form action="${pageContext.request.contextPath}/PaymentUpdateServlet" method="get" style="display:inline;">
              <input type="hidden" name="action"    value="update"/>
              <input type="hidden" name="paymentId" value="<%= p.getPaymentID() %>"/>
              <button type="submit" <%= !"PENDING".equals(p.getPaymentStatus()) ? "disabled" : "" %> > Update </button>            
            </form>

            <!-- DELETE button -->
            <form action="${pageContext.request.contextPath}/PaymentDeletionServlet" method="post" style="display:inline;">
              <input type="hidden" name="paymentId" value="<%= p.getPaymentID() %>"/>
              <button type="submit" <%= !"PENDING".equals(p.getPaymentStatus()) ? "disabled" : "" %> > Delete </button>
            </form>
          </div>
        <%   } // end for
          } %>
      </div>
      <br/>
    </div>
  </div>
</div>
</body>
</html>