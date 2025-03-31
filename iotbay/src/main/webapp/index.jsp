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
            
        </div>
    </div>
</body>
</html>
