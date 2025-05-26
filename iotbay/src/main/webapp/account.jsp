<%@ page import="model.users.*" %>
<%@ page session="true" %>
<%
    // Redirect to login page if user is not logged in
    User user = (User) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect("login.jsp");
        return;
    }
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
    <div class="header-right">
        <div class="nav-icons">
            <a href="${pageContext.request.contextPath}/account.jsp" title="Account" class="account-icon">
                <img src="${pageContext.request.contextPath}/assets/images/account_icon.png" alt="Account">
                <% if (user != null) { %>
                <span class="login-indicator"></span>
                <% } %>
            </a>
        </div>
        <div class="nav-icons">
            <a href="${pageContext.request.contextPath}/navigation.jsp" title="Navigation">
                <img src="${pageContext.request.contextPath}/assets/images/navigation_icon.png" alt="Navigation">
            </a>
        </div>
    </div>
</header>
<div class="container">
    <div class="main-container">
        <div class="centered-container">
            <form action="UserController" method="post">
                <h2>Account Management</h2>
                <label for="name">Name:</label><br>
                <input type="text" name="name" id="name" value="<%= user.getName() %>"><br>
                <label for="email">Email:</label><br>
                <input type="email" name="email" id="email" value="<%= user.getEmail() %>"><br>
                <label for="password">Password:</label><br>
                <input type="password" name="password" id="password" placeholder="Please set a new password!"
                       value="<%=user.getPassword() == null ? "" : user.getPassword() %>"><br>
                <input type="submit" value="Save Changes">
            </form>
            <div>
                <a href="LogOutServlet">Log Out</a>
            </div>
        </div>
    </div>
</div>
<div>
</div>
</body>
</html>