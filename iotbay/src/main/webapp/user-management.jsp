<%@ page import="model.users.*" %>
<%@ page session="true" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Account Management</title>
    <link rel="stylesheet" href="css/subpages/account.css">
    <%
        String userType = (String) session.getAttribute("userType");
        if (userType == null || !userType.equals("admin")) {
            response.sendRedirect("index.jsp");
            return;
        }
    %>
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
            <h1>User Management</h1>
            <div class="user-management">

            </div>
        </div>
    </div>
</div>
</body>
</html>