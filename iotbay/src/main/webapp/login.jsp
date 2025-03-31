<%@ page session="true" %>
<%
    if ("POST".equalsIgnoreCase(request.getMethod())) {
        // Placeholder for login logic
        session.setAttribute("user", new model.User(request.getParameter("email"), "Placeholder Name", "Placeholder Password", "male"));
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
    <h2>Login</h2>
    <form action="login.jsp" method="post">
        <label for="email">Email:</label><br>
        <input type="email" name="email" id="email" required><br>
        <label for="password">Password:</label><br>
        <input type="password" name="password" id="password" required><br>
        <input type="submit" value="Login">
    </form>
    <p>Don't have an account? <a href="register.jsp">Register here</a></p>
</body>
</html>