<%@ page import="utils.UserUtil" %>
<%@ page import="model.users.User" %>
<%@ page session="true" %>
<%
    // Check if logged in user is Admin
    User user = (User) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect("login.jsp");
        return;
    }
    else if (!UserUtil.isAdmin(user)) {
        response.sendRedirect("restricted.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>User Management</title>
    <link rel="stylesheet" href="css/subpages/user.css">
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
            <!-- Add content for user management here -->
        </div>
    </div>
</body>
</html>