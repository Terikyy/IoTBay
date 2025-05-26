<%@ page import="model.users.User" %>
<%@ page import="controllers.UserController" %>
<%@ page session="true" %>
<%
    // Check if a user is already logged in
    User user = (User) session.getAttribute("user");
    if (user != null) {
        response.sendRedirect("account.jsp");
        return;
    }
    String errorMessage = (String) session.getAttribute("error");
    if (errorMessage != null) {
        System.out.println(errorMessage);
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Register</title>
    <link rel="stylesheet" href="css/subpages/register.css">
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
            <form action="RegistrationController" method="post">
                <h2>Registration</h2>
                <label for="name">Name:</label><br>
                <input type="text" name="name" id="name" required><br>
                <label for="email">Email:</label><br>
                <input type="email" name="email" id="email" required><br>
                <label for="password">Password:</label><br>
                <input type="password" name="password" id="password" required><br>
                <input type="checkbox" name="tos" id="tos" required> I agree to the Terms of Service<br>
                <input type="submit" value="Register">
            </form>
        </div>
    </div>
</div>
</body>
</html>