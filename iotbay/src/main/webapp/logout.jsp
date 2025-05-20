<%@ page session="true" %>
<%
    // Remove the user from session storage
    session.removeAttribute("user");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Logout</title>
    <link rel="stylesheet" href="css/subpages/logout.css">
</head>
<body>
<header>
    <div class="logo">
        <img src="assets/images/iotbay_logo.png" alt="IoTBay">
    </div>
    <a href="index.jsp" title="Main Page">Return to Main Page</a>
</header>
<div class="container">
    <div class="main-container">
        <div class="centered-container">
            <h2>Logout Successful</h2>
            <p>You have been successfully logged out.</p>
            <a href="index.jsp" title="Main Page">Return to Main Page</a>
        </div>
    </div>
</div>
</body>
</html>