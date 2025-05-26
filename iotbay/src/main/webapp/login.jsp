<%@ page import="model.users.User" %>
<%@ page session="true" %>
<%
    // Check if a user is already logged in and if so redirect to welcome page
    User user = (User) session.getAttribute("user");
    if (user != null) {
        response.sendRedirect("account.jsp");
        return;
    }
    String error = (String) session.getAttribute("error");
    if (error != null) {
        session.removeAttribute("error");
    }
    if (error == null) {
        error = "";
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login</title>
    <link rel="stylesheet" href="css/subpages/login.css">
</head>
<body>
<header>
    <div class="logo">
        <a href="index.jsp" title="Main Page">
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
            <form action="LoginController" method="get">
                <h2>Login</h2>
                <div class="error-message">
                    <%= error %>
                </div>
                <label for="email">Email:</label><br>
                <input type="email" name="email" id="email" required><br>
                <label for="password">Password:</label><br>
                <input type="password" name="password" id="password"><br>
                <input type="submit">
            </form>
            <p>Don't have an account? <a href="register.jsp">Register here</a></p>
        </div>
    </div>
</div>
</body>
</html>