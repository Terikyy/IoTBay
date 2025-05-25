<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.AccessLog" %>
<%@ page import="java.util.List" %>
<%@ page session="true" %>

<%
    User user = (User) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    List<AccessLog> accessLogs = (List<AccessLog>) request.getAttribute("accessLogs");
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Access Logs</title>
    <link rel="stylesheet" href="css/subpages/accessLogs.css">
</head>
<body>
<header>
    <div class="logo">
        <img src="assets/images/iotbay_logo.png" alt="IoTBay">
    </div>
    <a href="account.jsp">Go to Account Management</a>
</header>

<div class="container">
    <h1>Access Logs</h1>
    <% if (accessLogs == null || accessLogs.isEmpty()) { %>
        <p>No access logs found.</p>
    <% } else { %>
        <table>
            <thead>
                <tr>
                    <th>Timestamp</th>
                    <th>Action</th>
                </tr>
            </thead>
            <tbody>
                <% for (AccessLog log : accessLogs) { %>
                    <tr>
                        <td><%= log.getTimestamp() %></td>
                        <td><%= log.getAction() %></td>
                    </tr>
                <% } %>
            </tbody>
        </table>
    <% } %>
</div>
</body>
</html>
