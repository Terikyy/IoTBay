<%@ page import="model.users.User" %>
<%@ page import="model.Product" %>
<%@ page import="java.util.List" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page session="true" %>
<%
    // Check if logged in user is Staff or Admin
    User user = (User) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    } else if (!user.isStaff() && !user.isAdmin()) {
        response.sendRedirect(request.getContextPath() + "/restricted.jsp");
        return;
    }

    // Redirect to inventory list if accessed directly
    if (request.getAttribute("products") == null) {
        response.sendRedirect(request.getContextPath() + "/products/inventory");
        return;
    }

    List<Product> products = (List<Product>) request.getAttribute("products");
    List<String> categories = (List<String>) request.getAttribute("categories");
    String message = (String) request.getAttribute("message");
    String error = (String) request.getAttribute("error");

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Inventory Management</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/subpages/inventory.css">
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
    <div class="main-container main-content">
        <div class="centered-container">
            <h1>Inventory Management</h1>

            <% if (message != null && !message.isEmpty()) { %>
            <div class="notification success"><%= message %>
            </div>
            <% } %>

            <% if (error != null && !error.isEmpty()) { %>
            <div class="notification error"><%= error %>
            </div>
            <% } %>

            <!-- Add New Product Form -->
            <h4>Add New Product</h4>
            <div class="product-form">
                <form action="${pageContext.request.contextPath}/products/inventory" method="post" class="product-grid">
                    <input type="hidden" name="action" value="add">

                    <input type="text" name="name" placeholder="Product Name" required>
                    <textarea name="description" placeholder="Description" required></textarea>
                    <input type="number" name="price" placeholder="Price" step="0.01" min="0" required>
                    <input type="number" name="stock" placeholder="Stock" min="0" required>
                    <select name="category">
                        <option value="">Select Category</option>
                        <% if (categories != null) { %>
                        <% for (String category : categories) { %>
                        <option value="<%= category %>"><%= category %>
                        </option>
                        <% } %>
                        <% } %>
                        <option value="new_category">New Category...</option>
                    </select>
                    <input type="text" name="newCategory" placeholder="New Category" style="display:none;">
                    <input type="date" name="releaseDate"
                           value="<%= dateFormat.format(new java.sql.Date(System.currentTimeMillis())) %>">

                    <div class="action-buttons">
                        <button type="submit" class="submit-button">Add</button>
                    </div>
                </form>
            </div>

            <!-- Product List Header -->
            <h4>Current Products</h4>
            <div class="product-grid column-header">
                <div>Name</div>
                <div>Description</div>
                <div>Price</div>
                <div>Stock</div>
                <div>Category</div>
                <div>Release Date</div>
                <div>Actions</div>
            </div>

            <!-- Product List -->
            <% if (products != null && !products.isEmpty()) {
                for (Product product : products) {
                    String formattedDate = product.getReleaseDate() != null ?
                            dateFormat.format(product.getReleaseDate()) :
                            dateFormat.format(new java.sql.Date(System.currentTimeMillis()));
            %>
            <div class="product-form">
                <form action="${pageContext.request.contextPath}/products/inventory" method="post" class="product-grid">
                    <input type="hidden" name="action" value="update">
                    <input type="hidden" name="productId" value="<%= product.getProductID() %>">

                    <input type="text" name="name" value="<%= product.getName() %>" required>
                    <textarea name="description" required><%= product.getDescription() %></textarea>
                    <input type="number" name="price" value="<%= product.getPrice() %>" step="0.01" min="0" required>
                    <input type="number" name="stock" value="<%= product.getStock() %>" min="0" required>
                    <select name="category">
                        <option value="">Select Category</option>
                        <% if (categories != null) { %>
                        <% for (String category : categories) { %>
                        <option value="<%= category %>" <%= category.equals(product.getCategory()) ? "selected" : "" %>><%= category %>
                        </option>
                        <% } %>
                        <% } %>
                        <option value="new_category">New Category...</option>
                    </select>
                    <input type="text" name="newCategory" placeholder="New Category" style="display:none;">
                    <input type="date" name="releaseDate" value="<%= formattedDate %>">

                    <div class="action-buttons">
                        <button type="submit" class="submit-button">Update</button>
                        <button type="submit" class="delete-button"
                                onclick="if(confirm('Are you sure you want to delete this product?')) {this.form.action.value='delete';} else {return false;}">
                            Delete
                        </button>
                    </div>
                </form>
            </div>
            <% }
            } else { %>
            <p>No products available.</p>
            <% } %>
        </div>
    </div>
</div>
<script>
    // Script to handle new category option
    document.querySelectorAll('select[name="category"]').forEach(select => {
        select.addEventListener('change', function () {
            const newCategoryInput = this.parentElement.querySelector('input[name="newCategory"]');
            if (this.value === 'new_category') {
                newCategoryInput.style.display = 'block';
                newCategoryInput.required = true;
            } else {
                newCategoryInput.style.display = 'none';
                newCategoryInput.required = false;
            }
        });
    });

    // Show notifications
    document.addEventListener('DOMContentLoaded', function () {
        const successNotification = document.querySelector('.notification.success');
        const errorNotification = document.querySelector('.notification.error');

        if (successNotification && successNotification.textContent.trim() !== '') {
            successNotification.style.display = 'block';
            setTimeout(() => {
                successNotification.style.display = 'none';
            }, 3000); // Hide after 3 seconds
        }

        if (errorNotification && errorNotification.textContent.trim() !== '') {
            errorNotification.style.display = 'block';
            setTimeout(() => {
                errorNotification.style.display = 'none';
            }, 3000); // Hide after 3 seconds
        }
    });
</script>
</body>
</html>