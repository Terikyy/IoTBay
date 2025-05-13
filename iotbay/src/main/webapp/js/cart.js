document.addEventListener('DOMContentLoaded', function() {
    // Get context path from meta tag
    const contextPath = document.querySelector('meta[name="contextPath"]')?.getAttribute('content') || '';
    
    // Add event listeners for all "Add to Cart" buttons
    const addToCartButtons = document.querySelectorAll('.add-to-cart');
    
    addToCartButtons.forEach(button => {
        button.addEventListener('click', function() {
            const productId = this.getAttribute('data-product-id');
            addToCart(productId, 1);
        });
    });
    
    // Add event listeners for cart page elements if they exist
    setupCartPageListeners();
    
    // Update cart count on page load
    updateCartCount();
    
    // Function to add a product to the cart
    function addToCart(productId, quantity) {
        fetch(`${contextPath}/cart/add`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: `productId=${productId}&quantity=${quantity}`
        })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                // Show success message
                showNotification('Product added to cart!');
                
                // Update cart count display
                updateCartCountDisplay(data.cartCount);
            } else {
                showNotification('Error: ' + data.message, 'error');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            showNotification('An error occurred while adding the product to the cart.', 'error');
        });
    }
    
    // Function to update a cart item
    function updateCartItem(productId, quantity) {
        fetch(`${contextPath}/cart/update`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: `productId=${productId}&quantity=${quantity}`
        })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                // Update cart count display
                updateCartCountDisplay(data.cartCount);
                
                // If on cart page, refresh cart content
                if (document.querySelector('.cart-items')) {
                    refreshCartPage();
                }
            } else {
                showNotification('Error: ' + data.message, 'error');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            showNotification('An error occurred while updating the cart.', 'error');
        });
    }
    
    // Function to remove a product from the cart
    function removeFromCart(productId) {
        fetch(`${contextPath}/cart/remove`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: `productId=${productId}`
        })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                // Show success message
                showNotification('Product removed from cart');
                
                // Update cart count display
                updateCartCountDisplay(data.cartCount);
                
                // If on cart page, refresh cart content
                if (document.querySelector('.cart-items')) {
                    refreshCartPage();
                }
            } else {
                showNotification('Error: ' + data.message, 'error');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            showNotification('An error occurred while removing the product from the cart.', 'error');
        });
    }
    
    // Function to clear the cart
    function clearCart() {
        if (!confirm('Are you sure you want to clear your cart?')) {
            return;
        }
        
        fetch(`${contextPath}/cart/clear`, {
            method: 'POST'
        })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                // Show success message
                showNotification('Cart cleared');
                
                // Update cart count display
                updateCartCountDisplay(0);
                
                // If on cart page, refresh cart content
                if (document.querySelector('.cart-items')) {
                    refreshCartPage();
                }
            } else {
                showNotification('Error: ' + data.message, 'error');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            showNotification('An error occurred while clearing the cart.', 'error');
        });
    }
    
    // Function to update the cart count display
    function updateCartCountDisplay(count) {
        const cartCount = document.querySelector('.cart-count');
        if (cartCount) {
            cartCount.textContent = count;
            
            // Show/hide the count badge
            if (count > 0) {
                cartCount.classList.remove('hidden');
            } else {
                cartCount.classList.add('hidden');
            }
        }
    }
    
    // Function to fetch and update cart count
    function updateCartCount() {
        fetch(`${contextPath}/cart/count`)
        .then(response => response.text())
        .then(count => {
            const cartCount = parseInt(count);
            if (!isNaN(cartCount) && cartCount > 0) {
                updateCartCountDisplay(cartCount);
            }
        })
        .catch(error => {
            console.error('Error fetching cart count:', error);
        });
    }
    
    // Function to refresh cart page content
    function refreshCartPage() {
        window.location.href = `${contextPath}/cart`;
    }
    
    // Function to setup listeners for cart page elements
    function setupCartPageListeners() {
        // Handle quantity changes
        const quantityInputs = document.querySelectorAll('.cart-quantity');
        if (quantityInputs && quantityInputs.length > 0) {
            quantityInputs.forEach(input => {
                input.addEventListener('change', function() {
                    const productId = this.getAttribute('data-product-id');
                    const quantity = parseInt(this.value);
                    
                    if (isNaN(quantity) || quantity < 1) {
                        this.value = 1;
                        updateCartItem(productId, 1);
                    } else {
                        updateCartItem(productId, quantity);
                    }
                });
            });
        }
        
        // Handle remove buttons
        const removeButtons = document.querySelectorAll('.remove-item');
        if (removeButtons && removeButtons.length > 0) {
            removeButtons.forEach(button => {
                button.addEventListener('click', function() {
                    const productId = this.getAttribute('data-product-id');
                    removeFromCart(productId);
                });
            });
        }
        
        // Handle clear cart button
        const clearCartButton = document.querySelector('.clear-cart');
        if (clearCartButton) {
            clearCartButton.addEventListener('click', clearCart);
        }
    }
    
    // Function to show a notification
    function showNotification(message, type = 'success') {
        // Create notification element if it doesn't exist
        let notification = document.querySelector('.notification');
        if (!notification) {
            notification = document.createElement('div');
            notification.className = 'notification';
            document.body.appendChild(notification);
        }
        
        // Set message and type
        notification.textContent = message;
        notification.className = 'notification ' + type;
        
        // Show notification
        notification.style.display = 'block';
        
        // Hide after 3 seconds
        setTimeout(() => {
            notification.style.display = 'none';
        }, 3000);
    }
});