<%@ page import="model.users.*" %>
<%@ page import="controllers.UserController" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="model.Log" %>
<%@ page import="controllers.LogController" %>
<%@ page import="java.text.DateFormatSymbols" %>
<%@ page import="java.util.*" %>
<%@ page import="model.Product" %>
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
            response.sendRedirect("restricted.jsp");
            return;
        }
        List<Log> logs;
        try {
            logs = LogController.queryLogs(query, request, response).reversed(); // reversed, so the latest logs are on top
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    %>
</head>
<body>
<header>
    <div class="logo">
        <a href="index.jsp" title="Main Page">
            <img src="${pageContext.request.contextPath}/assets/images/iotbay_logo.png" alt="IoTBay">
        </a>
    </div>
    <div class="search-container">
        <form action="log.jsp" method="get">
            <input type="text" class="search-input" name="query" placeholder="Search Logs..."
                   value="<%= request.getAttribute("query") != null ? request.getAttribute("query") : "" %>">
            <button type="submit" class="search-button">
                <img src="${pageContext.request.contextPath}/assets/images/search_icon.png" alt="Search">
            </button>
        </form>
    </div>
    <div class="header-right">
        <div class="nav-icons" title="Shopping Cart">
            <a href="${pageContext.request.contextPath}/cart" class="cart-button">
                <img src="${pageContext.request.contextPath}/assets/images/cart_icon.png" alt="Shopping Cart">
                <%
                    List<Map<String, Object>> cartItems = (List<Map<String, Object>>) request.getAttribute("cartItems");
                    int itemCount = 0;
                    if (cartItems != null) {
                        for (Map<String, Object> item : cartItems) {
                            itemCount += (int) item.get("quantity");
                        }
                    }
                %>
                <span class="cart-count <%= itemCount > 0 ? "" : "hidden" %>">
                        <%= itemCount %>
                    </span>
            </a>
        </div>
        <div class="nav-icons">
            <a href="${pageContext.request.contextPath}/account.jsp" title="Account" class="account-icon">
                <img src="${pageContext.request.contextPath}/assets/images/account_icon.png" alt="Account">
                <span class="login-indicator"></span>
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
    <div class="main-container main-content">
        <div class="logs-container">
            <h1 class="logs-title">Logs</h1>
            <div class="logs">
                <% if (logs.isEmpty()) { %>
                <div class="no-logs">No logs found</div>
                <% } else { %>
                <% for (int i = 0; i < logs.size(); i++) {
                    Log log = logs.get(i);
                    User user;
                    try {
                        user = UserController.getUserById(log.getUserId(), request, response);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                %>
                <div class="log-row">
                    <span class="log-timestamp"><%= new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(log.getTimestamp().getTime())) %></span>
                    <span class="log-user"><%= user != null ? user.getEmail() : "User does not exist" %></span>
                    <span class="log-message"><%= log.getMessage() %></span>
                </div>
                <% if (i < logs.size() - 1) { %>
                <div class="log-divider"></div>
                <% } %>
                <% } %>
                <% } %>
            </div>
        </div>
    </div>
</div>
</body>
</html>