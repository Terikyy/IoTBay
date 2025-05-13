document.addEventListener('DOMContentLoaded', function() {
    // Add event listeners for all "Add to Cart" buttons
    const addToCartButtons = document.querySelectorAll('.add-to-cart');
    
    addToCartButtons.forEach(button => {
        button.addEventListener('click', function() {
            const productId = this.getAttribute('data-product-id');
            addToCart(productId, 1);
        });
    });
    
    // Function to add a product to the cart
    function addToCart(productId, quantity) {
        // Get context path from a meta tag or directly from the page
        const contextPath = document.querySelector('meta[name="contextPath"]')?.getAttribute('content') || '';
        
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
                alert('Product added to cart!');
                // You could update a cart counter here if you have one
            } else {
                alert('Error: ' + data.message);
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('An error occurred while adding the product to the cart.');
        });
    }
});