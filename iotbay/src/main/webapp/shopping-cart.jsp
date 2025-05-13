<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="model.Product" %>
<%@ page import="java.text.DecimalFormat" %>
<%
    // Check if cart data was loaded through the servlet
    // If there's no request attribute for cartItems, redirect to the cart servlet
    if (request.getAttribute("cartItems") == null) {
        response.sendRedirect(request.getContextPath() + "/cart");
        return; // Important to stop JSP processing after redirect
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="contextPath" content="${pageContext.request.contextPath}">
    <title>Your Shopping Cart - IOTBay</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/subpages/shopping-cart.css">
</head>
<body>
<header>
    <div class="logo">
        <a href="${pageContext.request.contextPath}/products/list">
            <img src="${pageContext.request.contextPath}/assets/images/iotbay_logo.png" alt="IoTBay">
        </a>
    </div>
    <div class="header-right">
        <div class="account">
            <a href="${pageContext.request.contextPath}/account.jsp">
                <img src="${pageContext.request.contextPath}/assets/images/account_icon.png" alt="Account">
            </a>
        </div>
        <div class="shopping-cart">
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
    </div>
</header>

<div class="container">
    <div class="main-container">
        <h1>Your Shopping Cart</h1>
        
        <% 
        if (cartItems == null || cartItems.isEmpty()) { 
        %>
            <div class="empty-cart">
                <p>Your cart is empty.</p>
                <a href="${pageContext.request.contextPath}/products/list" class="continue-shopping">Continue Shopping</a>
            </div>
        <% } else { %>
            <div class="cart-container">
                <div class="cart-items">
                    <table>
                        <thead>
                            <tr>
                                <th>Product</th>
                                <th>Price</th>
                                <th>Quantity</th>
                                <th>Subtotal</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <% 
                            DecimalFormat df = new DecimalFormat("0.00");
                            
                            for (Map<String, Object> item : cartItems) {
                                Product product = (Product) item.get("product");
                                int quantity = (int) item.get("quantity");
                                double subtotal = (double) item.get("subtotal");
                            %>
                                <tr>
                                    <td class="product-info">
                                        <img src="${pageContext.request.contextPath}/assets/images/products/<%= product.getName() %>.png" 
                                             alt="<%= product.getName() %>"
                                             onerror="this.src='${pageContext.request.contextPath}/assets/images/products/placeholder.png';">
                                        <div>
                                            <h3><%= product.getName() %></h3>
                                            <p><%= product.getCategory() %></p>
                                        </div>
                                    </td>
                                    <td class="price">$<%= df.format(product.getPrice()) %></td>
                                    <td class="quantity">
                                        <input type="number" 
                                               min="1" 
                                               max="<%= product.getStock() %>" 
                                               value="<%= quantity %>" 
                                               class="cart-quantity" 
                                               data-product-id="<%= product.getProductID() %>">
                                    </td>
                                    <td class="subtotal">$<%= df.format(subtotal) %></td>
                                    <td class="actions">
                                        <button class="remove-item" data-product-id="<%= product.getProductID() %>">Remove</button>
                                    </td>
                                </tr>
                            <% } %>
                        </tbody>
                    </table>
                </div>
                
                <div class="cart-summary">
                    <h2>Order Summary</h2>
                    <div class="summary-row">
                        <span>Subtotal</span>
                        <span>$<%= df.format(request.getAttribute("cartTotal")) %></span>
                    </div>
                    <div class="summary-row">
                        <span>Shipping</span>
                        <span>Calculated at next step</span>
                    </div>
                    <div class="summary-row total">
                        <span>Total</span>
                        <span>$<%= df.format(request.getAttribute("cartTotal")) %></span>
                    </div>
                    <a href="${pageContext.request.contextPath}/order.jsp" class="checkout-button">Proceed to Checkout</a>
                    <button type="button" class="clear-cart">Clear Cart</button>
                    <a href="${pageContext.request.contextPath}/products/list" class="continue-shopping">Continue Shopping</a>
                </div>
            </div>
        <% } %>
    </div>
</div>

<!-- JavaScript for cart functionality -->
<script src="${pageContext.request.contextPath}/js/cart.js"></script>
</body>
</html>