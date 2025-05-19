<%@ page import="model.users.*" %>
<%@ page import="java.util.List" %>
<%@ page import="controllers.UserController" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="java.util.Map" %>
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
        if (admin == null || !admin.isAdmin()) {
            response.sendRedirect("login.jsp");
            return;
        }
        List<User> users = null;
        try {
            users = UserController.getAllUsers(request, response);
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
    <div class="account">
        <a href="${pageContext.request.contextPath}/account.jsp">
            <img src="${pageContext.request.contextPath}/assets/images/account_icon.png" alt="Account">
        </a>
    </div>
</header>
<div class="container">
    <div class="main-container">
        <div class="centered-container">
            <h1>User Management</h1>
            <div class="user-management">
                <% for (User user : users) {
                    if (user.getUserID() == admin.getUserID()) {
                        continue; // Skip the admin user
                    }
                %>
                <div class="user-card">
                    <form action="UserUpdateServlet" method="post">
                        <input type="hidden" name="userId" value="<%= user.getUserID() %>">
                        <label>
                            <input type="text" name="name" value="<%= user.getName() %>">
                        </label>
                        <label>
                            <input type="email" name="email" value="<%= user.getEmail() %>">
                        </label>
                        <label>
                            <select name="role">
                                <option value="customer" <%=!user.isStaff()%>>Customer</option>
                                <option value="staff" <%=user.isStaff()%>>Staff</option>
                            </select>
                        </label>
                        <button type="submit">Save</button>
                    </form>
                    <form action="UserDeletionServlet" method="post" class="delete-form">
                        <input type="hidden" name="userId" value="<%= user.getUserID() %>">
                        <button class="delete-button"
                                onclick="this.form.submit()">
                            Delete
                        </button>
                    </form>
                </div>
                <% } %>
                <h4>Add New User</h4>
                <div class="user-card">
                    <form action="UserCreationServlet" method="post">
                        <input type="email" id="email" name="email" placeholder="Email" required>
                        <input type="password" required name="password" id="password" placeholder="Password">
                        <select>
                            <option value="customer">Customer</option>
                            <option value="staff">Staff</option>
                        </select>
                        <button type="submit">Create</button>
                    </form>
                </div>

            </div>
        </div>
    </div>
</div>
</body>
</html>