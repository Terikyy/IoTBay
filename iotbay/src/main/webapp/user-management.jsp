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
                <% for (User user : users) { %>
                <div class="user-card">
                    Role: <%= user.isAdmin() ? "Admin" : user.isStaff() ? "Staff" : "Customer"%>
                    <h2><%= user.getName() %>
                    </h2>
                    <p><%= user.getEmail() %>
                    </p>
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