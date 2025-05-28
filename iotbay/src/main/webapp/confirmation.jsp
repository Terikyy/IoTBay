<%@ page session="true" %>
<%@ page import="model.users.User" %>
<% User user = (User) session.getAttribute("user"); %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Order confirmed</title>
    <!-- Reuse styling of restricted page -->
    <link rel="stylesheet" href="css/subpages/restricted.css">
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
            <h2>Order has been submitted!</h2>
            <p>Thank you for shopping with us!</p>
            <% if (user != null) { %>
                <a href="order.jsp" title="Orders">See my Orders</a>
            <% } else { %>
                <a href="index.jsp" title="Main Page">Go to Main Page</a>
            <% } %>
        </div>
    </div>
</div>
</body>
</html>