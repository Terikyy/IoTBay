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
        // Different comment to force merge conflict
        String query = request.getParameter("query");
        if (query == null) {
            query = "";
        }
        User admin = (User) session.getAttribute("user");
        if (admin == null || !admin.isAdmin()) {
            response.sendRedirect("login.jsp");
            return;
        }
        List<User> users = null;
        try {
            users = UserController.queryUsers(query, request, response);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if (users == null || users.isEmpty()) {
            response.sendRedirect("index.jsp");
        }
        String updateError = (String) session.getAttribute("update-error");
        if (updateError != null) {
            session.removeAttribute("update-error");
        }
        if (updateError == null) {
            updateError = "";
        }
        String error = (String) session.getAttribute("error");
        if (error != null) {
            session.removeAttribute("error");
        }
        if (error == null) {
            error = "";
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
        <form action="user-management.jsp" method="get">
            <input type="text" class="search-input" name="query" placeholder="Search..."
                   value="<%= query %>" autofocus>
            <button type="submit" class="search-button">
                <img src="${pageContext.request.contextPath}/assets/images/search_icon.png" alt="Search">
            </button>
        </form>
    </div>
    <div class="user-logs" title="User Logs">
        <a href="${pageContext.request.contextPath}/log.jsp">
            <img src="${pageContext.request.contextPath}/assets/images/log_icon.png" alt="Log">
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
                <%=updateError%>
            </p>
            <div class="user-management">
                <%= users.isEmpty() ? "No users found." : ""%>
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
                                <option value="customer" <%= user.isStaff() ? "" : "selected" %>>Customer</option>
                                <option value="staff" <%= user.isStaff() ? "selected" : "" %>>Staff</option>
                            </select>
                        </label>
                        <label>
                            <select name="active">
                                <option value="<%=true%>>" <%= user.isActive() ? "selected" : "" %>>Active</option>
                                <option value="<%=false%>>" <%= !user.isActive() ? "selected" : "" %>>Inactive</option>
                                ">
                            </select>
                        </label>
                        <input type="hidden" name="addressId" value="<%= user.getAddressID() %>">
                        <input type="hidden" name="password" value="<%=user.getPassword()%>">
                        <button type="submit" title="Save changes to this user">Save</button>
                    </form>
                    <form action="address.jsp" method="post" class="address-form">
                        <input type="hidden" name="userId" value="<%= user.getUserID() %>">
                        <button type="submit">
                            Address
                        </button>
                    </form>
                    <form action="ResetPasswordServlet" method="post" class="reset-form">
                        <input type="hidden" name="userId" value="<%= user.getUserID() %>">
                        <button class="reset-button"
                                onclick="this.form.submit()" title="Reset this user's password">
                            Reset
                        </button>
                    </form>
                    <form action="UserDeletionServlet" method="post" class="delete-form">
                        <input type="hidden" name="userId" value="<%= user.getUserID() %>">
                        <button class="delete-button"
                                onclick="this.form.submit()" title="Delete this user">
                            Delete
                        </button>
                    </form>
                </div>
                <% } %>
                <h4>Add New User</h4>
                <p class="error-message">
                    <%=error%>
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