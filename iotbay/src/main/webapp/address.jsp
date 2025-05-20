
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.Address" %>
<%@ page import="model.User" %>
<%@ page import="java.util.List" %>
<%@ page session="true" %>
<%@ page import="model.users.User" %>
<%
    // Redirect to login page if user is not logged in

    // Check if user session exists without using a User object
<%
    String username = (String) session.getAttribute("username");
    if (username == null) {
        response.sendRedirect("login.jsp");
        return;
    }
%>

// Retrieve the list of addresses from the request
    List<Address> addressList = (List<Address>) request.getAttribute("addressList");
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Address Management</title>
    <link rel="stylesheet" href="css/subpages/address.css">
</head>
<body>
<header>
    <div class="logo">
        <img src="assets/images/iotbay_logo.png" alt="IoTBay">
    </div>
    <a href="account.jsp">Return to Account Management</a>
</header>

<div class="container">
    <h1>Address Management</h1>

    <!-- Form for Adding a New Address -->
    <form action="address?action=insert" method="post">
        <h2>Add New Address</h2>
        <label for="name">Name:</label><br>
        <input type="text" id="name" name="name" required><br>
        <label for="streetNumber">Street Number:</label><br>
        <input type="number" id="streetNumber" name="streetNumber" required><br>
        <label for="streetName">Street Name:</label><br>
        <input type="text" id="streetName" name="streetName" required><br>
        <label for="postcode">Postcode:</label><br>
        <input type="number" id="postcode" name="postcode" required><br>
        <label for="suburb">Suburb:</label><br>
        <input type="text" id="suburb" name="suburb" required><br>
        <label for="city">City:</label><br>
        <input type="text" id="city" name="city" required><br>
        <label for="state">State:</label><br>
        <input type="text" id="state" name="state" required><br>
        <button type="submit">Add Address</button>
    </form>

    <!-- Table for Listing Existing Addresses -->
    <h2>Existing Addresses</h2>
    <table border="1">
        <thead>
            <tr>
                <th>ID</th>
                <th>Name</th>
                <th>Street Number</th>
                <th>Street Name</th>
                <th>Postcode</th>
                <th>Suburb</th>
                <th>City</th>
                <th>State</th>
                <th>Actions</th>
            </tr>
        </thead>
        <tbody>
            <%
                if (addressList != null) {
                    for (Address address : addressList) {
            %>
            <tr>
                <td><%= address.getId() %></td>
                <td><%= address.getName() %></td>
                <td><%= address.getStreetNumber() %></td>
                <td><%= address.getStreetName() %></td>
                <td><%= address.getPostcode() %></td>
                <td><%= address.getSuburb() %></td>
                <td><%= address.getCity() %></td>
                <td><%= address.getState() %></td>
                <td>
                    <a href="address?action=update&id=<%= address.getId() %>">Edit</a> |
                    <a href="address?action=delete&id=<%= address.getId() %>" onclick="return confirm('Are you sure?')">Delete</a>
                </td>
            </tr>
            <%
                    }
                } else {
            %>
            <tr>
                <td colspan="9">No addresses found.</td>
            </tr>
            <%
                }
            %>
        </tbody>
    </table>
</div>
</body>
</html>


