<%@ page import="model.users.User" %>
<%@ page import="model.Product" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page session="true" %>
<% User user = (User) session.getAttribute("user"); %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Navigation</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/subpages/navigation.css">
</head>
<body>
    <header>
        <div class="logo">
            <a href="${pageContext.request.contextPath}/products/list">
                <img src="${pageContext.request.contextPath}/assets/images/iotbay_logo.png" alt="IoTBay">
            </a>
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
        <div class="main-content navigation-content">   
            <!-- Customer accessible pages -->
            <h2 class="section-title">Main Services</h2>
            <div class="nav-grid">
                <a href="${pageContext.request.contextPath}/account.jsp" class="nav-tile">
                    <div class="icon-container">
                        <img src="${pageContext.request.contextPath}/assets/images/account_icon.png" alt="Account">
                    </div>
                    <h3>Account Management</h3>
                </a>
                
                <a href="${pageContext.request.contextPath}/order.jsp" class="nav-tile">
                    <div class="icon-container">
                        <img src="${pageContext.request.contextPath}/assets/images/order_icon.png" alt="Orders">
                    </div>
                    <h3>Orders</h3>
                </a>

                <a href="${pageContext.request.contextPath}/paymentList.jsp" class="nav-tile">
                    <div class="icon-container">
                        <img src="${pageContext.request.contextPath}/assets/images/payment_icon.png" alt="Payments">
                    </div>
                    <h3>Payments</h3>
                </a>
                
                <a href="${pageContext.request.contextPath}/shippingList.jsp" class="nav-tile">
                    <div class="icon-container">
                        <img src="${pageContext.request.contextPath}/assets/images/shipping_icon.png" alt="Shipments">
                    </div>
                    <h3>Shipments</h3>
                </a>
            </div>
            
            <% if (user != null && (user.isStaff() || user.isAdmin())) { %>
            <!-- Staff/Admin accessible pages -->
            <h2 class="section-title">Staff Services</h2>
            <div class="nav-grid">
                <a href="${pageContext.request.contextPath}/inventory.jsp" class="nav-tile">
                    <div class="icon-container">
                        <img src="${pageContext.request.contextPath}/assets/images/inventory_icon.png" alt="Inventory">
                    </div>
                    <h3>Inventory Management</h3>
                </a>
                
                <% if (user.isAdmin()) { %>
                <a href="${pageContext.request.contextPath}/user-management.jsp" class="nav-tile">
                    <div class="icon-container">
                        <img src="${pageContext.request.contextPath}/assets/images/manage_icon.png" alt="User Management">
                    </div>
                    <h3>User Management</h3>
                </a>
                <a href="${pageContext.request.contextPath}/log.jsp" class="nav-tile">
                    <div class="icon-container">
                        <img src="${pageContext.request.contextPath}/assets/images/logs_icon.png" alt="Logs">
                    </div>
                    <h3>Logs</h3>
                </a>
                <% } %>
            </div>
            <% } %>
        </div>
    </div>
</body>
</html>