<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>IOTBay</title>
    <link rel="stylesheet" href="css/index.css">
</head>
<body>
    <!-- Top search bar -->
    <header>
        <div class="logo">
            <a href="index.jsp">
                <img src="assets/images/iotbay_logo.png" alt="IoTBay">
            </a>
        </div>
        <div class="search-container">
            <input type="text" class="search-input" placeholder="Search...">
            <button class="search-button">
                <img src="assets/images/search_icon.png" alt="Search">
            </button>
        </div>
        <div class="account">
            <a href="account.jsp">
                <img src="assets/images/account_icon.png" alt="Account">
            </a>
        </div>
        <div class="shopping-cart">
            <a href="shopping-cart.jsp" class="cart-button">
                <img src="assets/images/cart_icon.png" alt="Shopping Cart">
            </a>
        </div>
        <div id="shopping-cart" class="cart-hidden">
            <h2>Shopping Basket</h2>
            <ul id="cart-items"></ul>
            <button id="checkout" onclick="window.location.href='checkout.jsp'">Checkout</button>
        </div>
    </header>

    <!-- Main container with sidebar and content -->
    <div class="container">
        <div class="sidebar">
            <nav>
                <ul class="menu">
                    <!-- Add categories here -->
                </ul>
            </nav>
        </div>

        <!-- Main content area -->
        <div class="main-content">
            <%-- Fetch products dynamically from the backend --%>
            <div class="product-tile" data-id="1000" data-name="Raspberry Pi Micro 2" data-price="29.99">
                <img src="assets/images/raspi_micro_2.png" alt="Raspberry Pi Micro 2" onerror="this.src='assets/images/placeholder.png';">
                <h3>Raspberry Pi Pico 2</h3>
                <p>$29.99</p>
                <p>Size: 1 Unit</p>
                <p>In Stock: 500</p>
                <button class="add-to-cart" data-id="1000">Add to Cart</button>
            </div>
        </div>
    </div>
</body>
</html>
