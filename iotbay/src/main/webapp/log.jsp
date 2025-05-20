<%@ page import="model.users.*" %>
<%@ page import="java.util.List" %>
<%@ page import="controllers.UserController" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="java.util.Map" %>
<%@ page import="model.Log" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="controllers.LogController" %>
<%@ page session="true" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>User Logs</title>
    <link rel="stylesheet" href="css/subpages/log.css">
    <%
        String query = request.getParameter("query");
        if (query == null) {
            query = "";
        }
        User admin = (User) session.getAttribute("user");
        if (admin == null || !admin.isAdmin()) {
            response.sendRedirect("login.jsp");
            return;
        }
        List<Log> logs = new ArrayList<>();
        try {
            logs = LogController.queryLogs(query, request, response);
        } catch (SQLException e) {
            throw new RuntimeException(e);
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
    <div class="manage-users" title="Manage Users">
        <a href="${pageContext.request.contextPath}/user-management.jsp">
            <img src="${pageContext.request.contextPath}/assets/images/manage_icon.png" alt="Manage Users">
        </a>
    </div>

    <div class="account">
        <a href="${pageContext.request.contextPath}/account.jsp">
            <img src="${pageContext.request.contextPath}/assets/images/account_icon.png" alt="Account">
        </a>
    </div>
</header>
<div class="container">
    <div class="main-container main-content">
        <div class="centered-container">
            <h1>User Management</h1>
            <p class="error-message">
            </p>
            <div class="user-management">
                <div class="user-card">
                    <form action="UserUpdateServlet" method="post">
                        <input type="hidden" name="userId" value="<%= "" %>">
                        <label>
                            <input type="text" name="name" value="<%= "" %>">
                        </label>
                        <label>
                            <input type="email" name="email" value="<%= "" %>">
                        </label>
                        <label>
                            <select name="role">
                                <option value="customer" <%= true ? "" : "selected" %>>Customer</option>
                                <option value="staff" <%= true ? "selected" : "" %>>Staff</option>
                            </select>
                        </label>
                        <input type="hidden" name="password" value="<%=""%>">
                        <button type="submit" title="Save changes to this user">Save</button>
                    </form>
                    <form action="ResetPasswordServlet" method="post" class="reset-form">
                        <input type="hidden" name="userId" value="<%= "" %>">
                        <button class="reset-button"
                                onclick="this.form.submit()" title="Reset this user's password">
                            Reset
                        </button>
                    </form>
                    <form action="UserDeletionServlet" method="post" class="delete-form">
                        <input type="hidden" name="userId" value="<%= "" %>">
                        <button class="delete-button"
                                onclick="this.form.submit()" title="Delete this user">
                            Delete
                        </button>
                    </form>
                </div>
                <h4>Add New User</h4>
                <p class="error-message">
                    
                </p>
                <div class="user-card">
                    <form action="UserCreationServlet" method="post">
                        <input type="text" id="name" name="name" placeholder="Name" required>
                        <input type="email" id="email" name="email" placeholder="Email" required>
                        <input type="password" name="password" id="password" placeholder="Password" required>
                        <select name="role" id="role" required>
                            <option value="customer">Customer</option>
                            <option value="staff">Staff</option>
                        </select>
                        <button type="submit" title="Create User">Create</button>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>