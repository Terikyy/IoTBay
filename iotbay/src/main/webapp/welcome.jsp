<%@ page import="model.users.User" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="model.Product" %>
<%@ page session="true" %>
<%
    // Redirect to login page if user is not logged in
    User user = (User) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect("login.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Welcome</title>
    <link rel="stylesheet" href="css/subpages/welcome.css">
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
            <h2>Login / Registration successful</h2>
            <p>Welcome, <%= user.getName() %>!</p>
            <p>You've been recognized as <%= user.getClass().getSimpleName() %>.</p>
            <a href="account.jsp">Proceed to Account Management</a>
        </div>
    </div>
</div>
</body>
</html>