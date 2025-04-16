<%@ page import="model.users.User" %>
<%@ page import="controllers.UserController" %>
<%@ page session="true" %>
<%
    // Check if a user is already logged in
    User loggedInUser = (User) session.getAttribute("user");
    if (loggedInUser != null) {
        response.sendRedirect("account.jsp");
        return;
    }
%>
<%
    // Maybe move handling to UserController
    if ("POST".equalsIgnoreCase(request.getMethod())) {
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        // Use UserController to register the customer
        User user = UserController.registerCustomer(name, email, password);

        if (user != null) {
            session.setAttribute("user", user);
            response.sendRedirect("welcome.jsp");
            return;
        } else {
            out.println("<p>An error occured while registering</p>");
        }
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