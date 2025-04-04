<%@ page import="models.users.Customer" %>
<%@ page import="models.users.User" %>
<%@ page import="java.util.Random" %>
<%@ page session="true" %>
<%
    if ("POST".equalsIgnoreCase(request.getMethod())) {
        User user = new Customer(
                (int) (Math.random() * 1000000), // Random ID for demo purposes - will be replaced with UUID logic or database ID in production
                request.getParameter("email"),
                request.getParameter("name"),
                request.getParameter("password")
        );
        session.setAttribute("user", user);
        response.sendRedirect("account.jsp");
        return;
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
        <img src="assets/images/iotbay_logo.png" alt="IoTBay">
    </div>
    <a href="index.jsp">Return to Main Page</a>
</header>
<div class="container">
    <div class="main-container">
        <div class="centered-container">
            <form action="register.jsp" method="post">
                <h2>Registration</h2>
                <label for="email">Email:</label><br>
                <input type="email" name="email" id="email" required><br>
                <label for="name">Name:</label><br>
                <input type="text" name="name" id="name" required><br>
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