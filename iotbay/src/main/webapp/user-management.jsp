<%@ page import="model.users.*" %>
<%@ page import="java.util.List" %>
<%@ page import="controllers.UserController" %>
<%@ page import="java.sql.SQLException" %>
<%@ page session="true" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>User Management</title>
    <link rel="stylesheet" href="css/subpages/user-management.css">
    <%
        User admin = (User) session.getAttribute("user");
        if (admin == null || !admin.isAdmin())
            response.sendRedirect("index.jsp");
        List<User> users = null;
        try {
            users = UserController.getAllUsers(session);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if (users == null || users.isEmpty()) {
            response.sendRedirect("index.jsp");
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
                <% for (User user : users) { %>
                <div class="user-card">
                    <h2><%= user.getName() %>
                    </h2>
                    <p><%= user.getEmail() %>
                    </p>
                    <form action="UserRoleServlet" method="post">
                        <input type="hidden" name="userId" value="<%= user.getUserID() %>">
                        <select name="userRole"
                                onchange="this.form.submit()"  <%=user.getUserID() == admin.getUserID() ? "disabled" : ""%>>
                            <option value="customer" <%= !user.isStaff() && !user.isAdmin() ? "selected" : ""%>>
                                Customer
                            </option>
                            <option value="staff" <%= user.isStaff() ? "selected" : "" %>>Staff</option>
                            <option value="admin" <%= user.isAdmin() ? "selected" : "" %>>Admin</option>
                        </select>
                    </form>

                    <% if (request.getAttribute("message") != null) { %>
                    <p><%= request.getAttribute("message") %>
                    </p>
                    <% } %>
                    <form action="UserDeletionServlet" method="post">
                        <input type="hidden" name="userId" value="<%= user.getUserID() %>">
                        <button onclick="this.form.submit()" <%=user.getUserID() == admin.getUserID() ? "disabled" : ""%>>
                            Delete
                        </button>
                    </form>
                </div>
                <% } %>
            </div>
        </div>
    </div>
</div>
</body>
</html>