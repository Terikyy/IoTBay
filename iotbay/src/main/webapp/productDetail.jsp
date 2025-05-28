<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="model.users.User" %>
<%@ page import="model.Product" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="contextPath" content="${pageContext.request.contextPath}">
    <title>Product Details - IOTBay</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/subpages/productDetail.css">
</head>
<body>
<%
    Product product = (Product) request.getAttribute("product");
    
    // Get the referrer URL for the back button
    String referer = request.getHeader("Referer");
    if (referer == null || referer.isEmpty()) {
        referer = request.getContextPath() + "/products/list";
    }

    User user = (User) session.getAttribute("user");
%>

<!-- Top search bar -->
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

<div class=main-content>
    <!-- Full-width content area -->
    <div class="full-container">
        <!-- Back button -->
        <div class="back-navigation">
            <a href="<%= referer %>" class="back-button">
                <span class="back-icon">←</span> Back to Products
            </a>
        </div>
        
        <!-- Product Detail -->
        <div class="product-detail">
            <div class="product-image-container">
                <img src="${pageContext.request.contextPath}/assets/images/products/<%= product.getName() %>.png"
                    alt="<%= product.getName() %>"
                    onerror="if (this.src.includes('.png')) { 
                                this.src='${pageContext.request.contextPath}/assets/images/products/<%= product.getName() %>.jpg';
                            } else if (this.src.includes('.jpg')) {
                                this.src='${pageContext.request.contextPath}/assets/images/products/<%= product.getName() %>.jpeg';
                            } else {
                                this.src='${pageContext.request.contextPath}/assets/images/products/placeholder.png';
                            }"
                    class="product-image">
            </div>
            <div class="product-info">
                <h1><%= product.getName() %></h1>
                <div class="product-category"><%= product.getCategory() %></div>
                <div class="product-price">$<%= String.format("%.2f", product.getPrice()) %></div>
                <div class="product-stock">
                    <% if (product.getStock() > 0) { %>
                        <span class="in-stock">In Stock: <%= product.getStock() %></span>
                    <% } else { %>
                        <span class="out-of-stock">Out of Stock</span>
                    <% } %>
                </div>
                <div class="product-description">
                    <h3>Description:</h3>
                    <p><%= product.getDescription() != null ? product.getDescription() : "No description available." %></p>
                </div>
                <div class="product-actions">
                    <input type="number" min="1" max="<%= product.getStock() %>" value="1" class="quantity-input" id="quantity">
                    <button 
                        class="add-to-cart" 
                        data-product-id="<%= product.getProductID() %>"
                        data-stock="<%= product.getStock() %>"
                        <%= product.getStock() <= 0 ? "disabled" : "" %>
                    >
                        <%= product.getStock() > 0 ? "Add to Cart" : "Out of Stock" %>
                    </button>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- JavaScript for cart functionality -->
<script src="${pageContext.request.contextPath}/js/cart.js"></script>
</body>
</html>