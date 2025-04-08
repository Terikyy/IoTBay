<%@ page import="model.users.*" %>
<%@ page session="true" %>
<%
    // Add support for other user types
    Customer customer = (Customer) session.getAttribute("customer");
    if (customer != null) {
        response.sendRedirect("account.jsp");
        return;
    }
%>
<%
    if ("POST".equalsIgnoreCase(request.getMethod())) {
        // Placeholder for login logic
        session.setAttribute("customer", new Customer(1, "Placeholder Name", request.getParameter("email"), request.getParameter("password")));
        response.sendRedirect("account.jsp");
        return;
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
            <img src="assets/images/iotbay_logo.png" alt="IoTBay">
        </div>
        <a href="index.jsp">Return to Main Page</a>
    </header>
    <div class="container">
        <div class="main-container">
            <div class="centered-container">
                <form action="login.jsp" method="post">
                    <h2>Login</h2>
                    <label for="email">Email:</label><br>
                    <input type="email" name="email" id="email" required><br>
                    <label for="password">Password:</label><br>
                    <input type="password" name="password" id="password" required><br>
                    <input type="submit" value="Login">
                </form>
                <p>Don't have an account? <a href="register.jsp">Register here</a></p>
            </div>
        </div>
    </div>
</body>
</html>