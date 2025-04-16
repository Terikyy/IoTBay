<%@ page import="controllers.UserController" %>
<%@ page import="model.users.User" %>
<%@ page session="true" %>
<%
    // Check if a user is already logged in and if so redirect to welcome page
    User user = (User) session.getAttribute("user");
    if (user != null) {
        response.sendRedirect("welcome.jsp");
        return;
    }
%>
<%
    if ("POST".equalsIgnoreCase(request.getMethod())) {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        // Authenticate user using UserController
        User loggedInUser = UserController.authenticateUser(email, password);

        if (loggedInUser != null) {
            // Store user in session and redirect to welcome page
            session.setAttribute("user", loggedInUser);
            response.sendRedirect("welcome.jsp");
            return;
        } else {
            out.println("<p>Invalid email or password. Please try again.</p>");
        }
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