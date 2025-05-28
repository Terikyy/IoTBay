<%@ page import="model.users.User" %>
<%@ page import="controllers.UserController" %>
<%@ page import="model.Payment" %>
<%@ page import="controllers.PaymentController" %>
<%@ page import="model.dao.PaymentDAO" %>
<%@ page session="true" %>
<%@ page import="model.Order" %>
<%@ page import="controllers.OrderController" %>
<%@ page import="java.sql.SQLException" %>


<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Payment</title>
    <link rel="stylesheet" href="css/subpages/payment.css">

    <%
        String message = (String) request.getAttribute("message");
        String errorMessage = (String) request.getAttribute("errorMessage");


        User user = (User) session.getAttribute("user");
        Integer orderIdObj = (Integer) session.getAttribute("orderId");
        if (orderIdObj == null) {
            // no orderId in session ⇒ redirect or show error
            response.sendRedirect("orderSummary.jsp");
            return;
        }
        int orderId = orderIdObj;

        Order order = null;
        try {
            order = OrderController.getOrderById(orderId, request, response);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    %>

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
            <h2>Payment</h2>

            <form action="PayPalServlet" method="post" class="payment-method-form" style="margin-top: 1rem;">
                <div class="form-group">
                    <label for="paymentMethod">Payment Method:</label>
                    <select name="paymentMethod" id="paymentMethod" required onchange="this.form.submit()">
                        <option value="credit-card" selected>Credit Card</option>
                        <option value="paypal">PayPal</option>
                    </select>
                </div>
            </form>

            <form action="PaymentController" method="post">
                <div id="cardDetails">
                    <div class="form-group">
                        <label for="nameOnCard">Name on Card:</label>
                        <input type="text" name="nameOnCard" id="nameOnCard" required>
                    </div>

                    <div class="form-group">
                        <label for="cardNumber">Card Number:</label>
                        <input type="number" name="cardNumber" id="cardNumber" required>
                    </div>

                    <div class="form-group">
                        <label for="expiryDate">Expiry Date:</label>
                        <input type="date" name="expiryDate" id="expiryDate" required>
                    </div>

                    <div class="form-group">
                        <label for="cvv">CVV:</label>
                        <input type="number" name="cvv" id="cvv" required>
                    </div>
                </div>

                <div class="totalPrice">
                    <strong>Total Amount: $<%= String.format("%.2f", order.getTotalPrice()) %>
                    </strong>
                </div>

                <div class="button-group">
                    <button type="submit" name="action" value="Save" <%= (user == null ? "disabled" : "") %> >Save
                    </button>
                    <button type="submit" name="action" value="Pay Now">Pay Now</button>
                </div>

                <%-- validation errors, plain text styled as .error-message --%>
                    <% if (errorMessage != null) { %>
                    <div class="error-message"><%= errorMessage %></div>
                <% } %>

                <% if (message != null) {
                    session.removeAttribute("message");
                %>
                <div class="message" style="cursor: pointer;"
                     onclick="window.location.href='${pageContext.request.contextPath}/shipping.jsp';"><%=message%>
                </div>
                <% } %>

                <% if (user == null) { %>
                <a href="${pageContext.request.contextPath}/login.jsp">
                    <p class="message">You must be logged in to save payments.</p>
                </a>
                <% } %>

            </form>
        </div>
    </div>
</div>
</body>
</html>