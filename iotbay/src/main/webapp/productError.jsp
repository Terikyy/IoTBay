<%@ page session="true" %>
<%@ page import="model.users.User" %>
<% 
    User user = (User) session.getAttribute("user"); 
    String errorTitle = (String) request.getAttribute("errorTitle");
    String errorMessage = (String) request.getAttribute("errorMessage");
    
    // Default values if not set
    if (errorTitle == null) errorTitle = "Product Error";
    if (errorMessage == null) errorMessage = "The requested product could not be found.";
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><%= errorTitle %> - IOTBay</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/subpages/restricted.css">
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
            <h2><%= errorTitle %></h2>
            <p><%= errorMessage %></p>
            <a href="${pageContext.request.contextPath}/products/list" class="back-button">
                Browse Available Products
            </a>
        </div>
    </div>
</div>
</body>
</html>