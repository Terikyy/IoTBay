<%@ page import="model.users.*" %>
<%@ page import="model.Address" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
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
        <a href="${pageContext.request.contextPath}/products/list">
            <img src="${pageContext.request.contextPath}/assets/images/iotbay_logo.png" alt="IoTBay">
        </a>
    </div>


    <div class="search-container">
        <form action="${pageContext.request.contextPath}/products/list" method="get">
            <input type="text" class="search-input" name="query" placeholder="Search..."
                   value="<%= request.getAttribute("searchQuery") != null ? request.getAttribute("searchQuery") : "" %>">
            <% if (request.getAttribute("selectedCategory") != null && !request.getAttribute("selectedCategory").toString().isEmpty()) { %>
            <input type="hidden" name="category" value="<%= request.getAttribute("selectedCategory") %>">
            <% } %>
            <button type="submit" class="search-button">
                <img src="${pageContext.request.contextPath}/assets/images/search_icon.png" alt="Search">
            </button>
        </form>
    </div>

    <%
        if (user.isAdmin()) {
    %>
    <div class="user-logs" title="User Logs">
        <a href="${pageContext.request.contextPath}/log.jsp">
            <img src="${pageContext.request.contextPath}/assets/images/log_icon.png" alt="Log">
        </a>
    </div>
    <div class="manage-users" title="Manage Users">
        <a href="${pageContext.request.contextPath}/user-management.jsp">
            <img src="${pageContext.request.contextPath}/assets/images/manage_icon.png" alt="Manage Users">
        </a>
    </div>
    <% } %>

    <div class="shopping-cart"> <!-- Reusing same style for Shipping List Icon (Add by Jiaming) -->
        <a href="${pageContext.request.contextPath}/shippingList.jsp">
            <img src="${pageContext.request.contextPath}/assets/images/shipping_icon.png" alt="ShippingList">
        </a>
    </div>

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
                <input type="password" name="password" id="password" placeholder="Please set a new password!"
                       value="<%=user.getPassword() == null ? "" : user.getPassword() %>"><br>
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
            <div>
                <a href="logout.jsp">Logout</a>
            </div>
            <div>
                <!-- Add the Manage Addresses button -->
                <a href="address.jsp">Address Management</a>
            </div>
        </div>
    </div>
</div>
<div>
</div>
</body>
</html>