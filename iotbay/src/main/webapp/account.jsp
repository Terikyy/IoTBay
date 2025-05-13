<%@ page import="model.users.*" %>
<%@ page import="utils.UserUtil" %>
<%@ page import="model.Address" %>
<%@ page session="true" %>
<%
    // Redirect to login page if user is not logged in
    User user = (User) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    Address address = (Address) session.getAttribute("address");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Account Management</title>
    <link rel="stylesheet" href="css/subpages/account.css">
</head>
<body>
<header>
    <div class="logo">
        <img src="assets/images/iotbay_logo.png" alt="IoTBay">
    </div>
    <%
        // Check if the user is an admin
        if (user.isAdmin()) {
    %>
    <a href="user-management.jsp">Manage Users</a>
    <%
        }
    %>
    <a href="index.jsp">Return to Main Page</a>
</header>
<div class="container">
    <div class="main-container">
        <div class="centered-container">
            <form action="UserController" method="post">
                <h2>Account Management</h2>
                <p>Personal Information:</p>
                <label for="name">Name:</label><br>
                <input type="text" name="name" id="name" value="<%= user.getName() %>"><br>
                <label for="email">Email:</label><br>
                <input type="email" name="email" id="email" value="<%= user.getEmail() %>" readonly><br>
                <label for="password">Password:</label><br>
                <input type="password" name="password" id="password" value="<%= user.getPassword() %>"><br>
                <p>Address Information:</p>
                <!-- TODO: Add adress information fields with Adress.java class-->
                <label for="streetName">Street Name:</label><br>
                <input type="text" name="streetName" id="streetName"
                       value="<%=address == null ? "" : address.getStreetName()%>"><br>
                <label for="streetNumber">Street Number:</label><br>
                <input type="number" name="streetNumber" id="streetNumber"
                       value="<%=address == null ? "" : address.getStreetNumber()%>"><br>
                <label for="suburb">Suburb:</label><br>
                <input type="text" name="suburb" id="suburb"
                       value="<%=address == null ? "" : address.getSuburb()%>"><br>
                <label for="postalCode">Postal Code:</label><br>
                <input type="number" name="postalCode" id="postalCode"
                       value="<%=address == null ? "" : address.getPostcode()%>"><br>
                <label for="city">City:</label><br>
                <input type="text" name="city" id="city" value="<%=address == null ? "" : address.getCity()%>"><br>
                <label for="state">State:</label><br>
                <input type="text" name="state" id="state" value="<%=address == null ? "" : address.getState()%>"><br>
                <input type="submit" value="Save Changes">
            </form>
            <a href="logout.jsp">Logout</a>
        </div>
    </div>
</div>
</body>
</html>