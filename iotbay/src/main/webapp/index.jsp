<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="model.users.User" %>
<%
    // Redirect to products list if accessed directly
    if (request.getAttribute("products") == null) {
        response.sendRedirect(request.getContextPath() + "/products/list");
        return;
    }
%>
<% User user = (User) session.getAttribute("user"); %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="contextPath" content="${pageContext.request.contextPath}">
    <title>IOTBay</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/index.css">
</head>
<body>
<!-- Top search bar -->
<header>
    <div class="logo">
        <a href="${pageContext.request.contextPath}/products/list">
            <img src="${pageContext.request.contextPath}/assets/images/iotbay_logo.png" alt="IoTBay">
        </a>
    </div>
    <div class="search-container">
        <form action="${pageContext.request.contextPath}/products/list" method="get">
            <input type="text" class="search-input" name="query" placeholder="Search Products..."
                   value="<%= request.getAttribute("searchQuery") != null ? request.getAttribute("searchQuery") : "" %>">
            <% if (request.getAttribute("selectedCategory") != null && !request.getAttribute("selectedCategory").toString().isEmpty()) { %>
            <input type="hidden" name="category" value="<%= request.getAttribute("selectedCategory") %>">
            <% } %>
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

<!-- Main container with sidebar and content -->
<div class="container">
    <div class="sidebar">
        <nav>
            <ul class="menu">
                <!-- Show "All Products" option -->
                <li>
                    <a href="${pageContext.request.contextPath}/products/list"
                       class="<%= request.getAttribute("selectedCategory") == null || request.getAttribute("selectedCategory").toString().isEmpty() ? "active" : "" %>">All
                        Products</a>
                </li>
                <!-- List all categories dynamically -->
                <%
                    java.util.List<String> categories = (java.util.List<String>) request.getAttribute("categories");
                    if (categories != null) {
                        for (String category : categories) {
                %>
                <li>
                    <a href="${pageContext.request.contextPath}/products/list?category=<%= category %>"
                       class="<%= category.equals(request.getAttribute("selectedCategory")) ? "active" : "" %>"><%= category %>
                    </a>
                </li>
                <%
                        }
                    }
                %>
            </ul>
        </nav>
    </div>

    <!-- Main content area -->
    <div class="main-content">
        <!-- Display current category if any -->
        <% if (request.getAttribute("selectedCategory") != null && !request.getAttribute("selectedCategory").toString().isEmpty()) { %>
        <h2>Category: <%= request.getAttribute("selectedCategory") %>
        </h2>
        <% } %>

        <!-- Display search results if searching -->
        <% if (request.getAttribute("searchQuery") != null && !request.getAttribute("searchQuery").toString().isEmpty()) { %>
        <h2>Search Results for: <%= request.getAttribute("searchQuery") %>
        </h2>
        <% } %>

        <!-- Display message if no products found -->
        <%
            java.util.List<?> products = (java.util.List<?>) request.getAttribute("products");
            if (products == null || products.isEmpty()) {
        %>
        <p>No products found.</p>
        <% } else { %>

        <!-- Display products -->
        <%
            for (Object productObj : products) {
                model.Product product = (model.Product) productObj;
        %>
        <div class="product-tile">
            <img src="${pageContext.request.contextPath}/assets/images/products/<%= product.getName() %>.png"
                 alt="<%= product.getName() %>"
                 onerror="if (this.src.includes('.png')) {
                         this.src='${pageContext.request.contextPath}/assets/images/products/<%= product.getName() %>.jpg';
                         } else if (this.src.includes('.jpg')) {
                         this.src='${pageContext.request.contextPath}/assets/images/products/<%= product.getName() %>.jpeg';
                         } else {
                         this.src='${pageContext.request.contextPath}/assets/images/products/placeholder.png';
                         }">
            <h3><%= product.getName() %>
            </h3>
            <p>$<%= product.getPrice() %>
            </p>
            <p>In Stock: <%= product.getStock() %>
            </p>
            <a href="${pageContext.request.contextPath}/products/detail/<%= product.getProductID() %>"
               class="view-details">View Details</a>
            <button
                    class="add-to-cart"
                    data-product-id="<%= product.getProductID() %>"
                    data-stock="<%= product.getStock() %>"
                    <%= product.getStock() <= 0 ? "disabled" : "" %>
            >
                <%= product.getStock() > 0 ? "Add to Cart" : "Out of Stock" %>
            </button>
        </div>
        <%
                }
            }
        %>
    </div>
</div>

<!-- JavaScript for cart functionality -->
<script src="${pageContext.request.contextPath}/js/cart.js"></script>
</body>
</html>
