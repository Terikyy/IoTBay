<%@ page import="model.Payment" %>
<%@ page import="model.users.User" %>
<%@ page session="true" %>

<% User user = (User) session.getAttribute("user"); %>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Update Payment</title>
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

            <%
            Payment p = (Payment) request.getAttribute("payment");
            %>

            <h2>Update Payment</h2>
            
            <p><strong>Payment ID: #<%= p.getPaymentID() %></strong></p>
            <p>Order ID: <%= p.getOrderID() %></p>

            <input type="hidden" name="paymentId" value="<%= p.getPaymentID() %>"/>

            
            <form action="PayPalServlet" method="post" class="payment-method-form" style="margin-top: 1rem;">
                <div class="form-group">
                    <label for="paymentMethod">Payment Method:</label>
                    <select name="paymentMethod" id="paymentMethod" required onchange="this.form.submit()">
                        <option value="credit-card" selected>Credit Card</option>
                        <option value="paypal">PayPal</option>
                    </select>
            </div>
            </form>

            <form action="PaymentUpdateServlet" method="post" class="payment-method-form">
                <input type="hidden" name="paymentId" value="<%= p.getPaymentID() %>"/>
          
                <div id="cardDetails">
                    <div class="form-group">
                    <label for="nameOnCard">Name on Card:</label>
                    <input type="text" id="nameOnCard" name="nameOnCard" required>
                    </div>

                    <div class="form-group">
                    <label for="cardNumber">Card Number:</label>
                    <input type="number" id="cardNumber" name="cardNumber" required>
                    </div>

                    <div class="form-group">
                    <label for="expiryDate">Expiry Date:</label>
                    <input type="date" id="expiryDate" name="expiryDate" required>
                    </div>

                    <div class="form-group">
                    <label for="cvv">CVV:</label>
                    <input type="number" id="cvv" name="cvv" required>
                    </div>
                </div>

                <div class="totalPrice">
                    <strong>Total Amount: $<%= String.format("%.2f", p.getAmountPaid()) %></strong>
                </div>

                <div class="button-group">
                    <button type="button" onclick="window.location.href='${pageContext.request.contextPath}/PaymentListController';" >Cancel </button>
                    <button type="submit">Pay Now</button>
                </div>
        </form>
      </div>
    </div>
  </div>
</body>
</html>