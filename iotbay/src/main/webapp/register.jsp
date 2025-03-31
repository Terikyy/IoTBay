<%@ page import="model.User" %>
<%@ page session="true" %>
<%
    if ("POST".equalsIgnoreCase(request.getMethod())) {
        User user = new User(
            request.getParameter("email"),
            request.getParameter("name"),
            request.getParameter("password"),
            request.getParameter("gender")
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
    <h2>Registration</h2>
    <form action="register.jsp" method="post">
        <label for="email">Email:</label><br>
        <input type="email" name="email" id="email" required><br>
        <label for="name">Name:</label><br>
        <input type="text" name="name" id="name" required><br>
        <label for="password">Password:</label><br>
        <input type="password" name="password" id="password" required><br>
        <label for="gender">Gender:</label><br>
        <input type="radio" name="gender" value="male" required> Male<br>
        <input type="radio" name="gender" value="female" required> Female<br>
        <input type="radio" name="gender" value="other" required> Other<br>
        <input type="checkbox" name="tos" id="tos" required> I agree to the Terms of Service<br>
        <input type="submit" value="Register">
    </form>
</body>
</html>