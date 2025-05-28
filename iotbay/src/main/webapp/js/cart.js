document.addEventListener('DOMContentLoaded', function() {
    // Get context path from meta tag
    const contextPath = document.querySelector('meta[name="contextPath"]')?.getAttribute('content') || '';
    
    // Check for stored notification message
    const storedNotification = sessionStorage.getItem('cartNotification');
    if (storedNotification) {
        try {
            const notification = JSON.parse(storedNotification);
            showNotification(notification.message, notification.type);
            // Clear after showing
            sessionStorage.removeItem('cartNotification');
        } catch (e) {
            console.error('Error parsing stored notification', e);
        }
    }
    
    // Add event listeners for all "Add to Cart" buttons
    const addToCartButtons = document.querySelectorAll('.add-to-cart');
    
    addToCartButtons.forEach(button => {
        // Check stock and disable button if zero
        const stock = parseInt(button.getAttribute('data-stock') || '0');
        if (stock <= 0) {
            button.disabled = true;
            button.textContent = 'Out of Stock';
            button.classList.add('out-of-stock');
        }
        
        button.addEventListener('click', function() {
            const productId = this.getAttribute('data-product-id');
            
            // Check for the quantity input on product detail page
            const quantityInput = document.getElementById('quantity');
            let quantity = 1;
            
            // If quantity input exists, use its value
            if (quantityInput) {
                quantity = Math.max(1, parseInt(quantityInput.value) || 1);
            }
            
            addToCart(productId, quantity);
        });
    });
    
    // Add event listeners for cart page elements if they exist
    setupCartPageListeners();
    
    // Update cart count on page load
    updateCartCount();
    
    // Function to add a product to the cart
    function addToCart(productId, quantity) {
        // First, check the current cart quantity for this product
        fetch(`${contextPath}/cart/check?productId=${productId}`)
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                const currentCartQuantity = data.currentQuantity || 0;
                const availableStock = data.availableStock || 0;
                
                // Check if requested quantity would exceed stock
                if (currentCartQuantity + quantity > availableStock) {
                    // Calculate how many more items can be added
                    const canAdd = Math.max(0, availableStock - currentCartQuantity);
                    
                    if (canAdd === 0) {
                        showNotification('This item is already at maximum available quantity in your cart.', 'warning');
                    } else {
                        // Auto-adjust to max available and notify user (no confirm dialog)
                        addItemToCart(productId, canAdd, `Only ${canAdd} more units available. Added to cart.`);
                    }
                } else {
                    // If stock is sufficient, proceed with adding to cart
                    addItemToCart(productId, quantity);
                }
            } else {
                showNotification('Error checking product availability: ' + data.message, 'error');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            showNotification('An error occurred while checking product availability.', 'error');
        });
    }

    // Helper function to actually add the item to cart after validation
    function addItemToCart(productId, quantity, customMessage = null) {
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
                // Show success message or custom message
                showNotification(customMessage || 'Product added to cart!', 
                                customMessage ? 'warning' : 'success');
                
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
    
    // Helper function to actually update the item quantity after validation
    function updateCartItemQuantity(productId, quantity) {
        // First, check available stock
        fetch(`${contextPath}/cart/check?productId=${productId}`)
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                const availableStock = data.availableStock || 0;
                
                // If requested quantity exceeds stock, adjust to max available
                if (quantity > availableStock) {
                    // Update input field to show correct value
                    const quantityInput = document.querySelector(`.cart-quantity[data-product-id="${productId}"]`);
                    if (quantityInput) {
                        quantityInput.value = availableStock;
                    }
                    
                    // Send the request with adjusted quantity
                    sendUpdateRequest(productId, availableStock, 
                        `Quantity adjusted to ${availableStock} (maximum available stock).`);
                } else {
                    // If stock is sufficient, proceed with normal update
                    sendUpdateRequest(productId, quantity);
                }
            } else {
                showNotification('Error checking product availability: ' + data.message, 'error');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            showNotification('An error occurred while checking product availability.', 'error');
        });
    }
    
    // Helper function to send the update request
    function sendUpdateRequest(productId, quantity, customMessage = null) {
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
                // Show success message or custom message
                showNotification(customMessage || 'Quantity updated successfully',
                                customMessage ? 'warning' : 'success');
                
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
            console.error('Error updating cart:', error);
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
        // Store notification message in sessionStorage before redirect
        if (lastNotificationMessage) {
            sessionStorage.setItem('cartNotification', JSON.stringify({
                message: lastNotificationMessage,
                type: lastNotificationType
            }));
        }
        // Then redirect
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
                        updateCartItemQuantity(productId, 1);
                    } else {
                        updateCartItemQuantity(productId, quantity);
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
        // Store the message and type for potential page refresh
        lastNotificationMessage = message;
        lastNotificationType = type;
        
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
            notification.style.opacity = '0';
            notification.style.transform = 'translateX(100%)';
            notification.style.transition = 'opacity 0.3s, transform 0.3s';
            
            setTimeout(() => {
                notification.style.display = 'none';
                notification.style.opacity = '1';
                notification.style.transform = 'translateX(0)';
                notification.style.transition = '';
            }, 300);
        }, 3000);
    }
});