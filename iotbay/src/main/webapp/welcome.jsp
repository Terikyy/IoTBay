<%@ page import="model.users.User" %>
<%@ page import="utils.UserUtil" %>
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
        <img src="assets/images/iotbay_logo.png" alt="IoTBay">
    </div>
    <a href="index.jsp">Return to Main Page</a>
</header>
<div class="container">
    <div class="main-container">
        <div class="centered-container">
            <h2>Login / Registration successful</h2>
            <p>Welcome, <%= user.getName() %>!</p>
            <p>You've been recognized as <%= user.getClass().getSimpleName() %>.</p>
            <a href="account.jsp">Proceed to Account Management</a>
            <a href="index.jsp">Return to Main Page</a>
        </div>
    </div>
</div>
</body>
</html>