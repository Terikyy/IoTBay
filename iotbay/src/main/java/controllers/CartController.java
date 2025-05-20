package controllers;

import model.Product;
import model.dao.CartItemDAO;
import model.dao.ProductDAO;
import model.lineproducts.CartItem;
import model.users.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller servlet for managing shopping cart operations.
 * Handles cart display, adding/updating/removing items, and synchronization
 * between session-based guest carts and database-stored user carts.
 * Maps to URL pattern "/cart/*" for all cart-related actions.
 */
@WebServlet("/cart/*")
public class CartController extends HttpServlet {
    
    /**
     * Handles HTTP GET requests for cart operations.
     * Supports displaying the cart and retrieving cart item count via AJAX.
     *
     * @param request The HTTP request object
     * @param response The HTTP response object
     * @throws ServletException If a servlet-specific error occurs
     * @throws IOException If an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        // Ensure ConnServlet has been called to initialize DAOs
        if (session.getAttribute("cartItemDAO") == null || session.getAttribute("productDAO") == null) {
            // Pass the current request URL as a parameter for redirect after initialization
            String currentURL = request.getRequestURI();
            if (request.getQueryString() != null) {
                currentURL += "?" + request.getQueryString();
            }
            response.sendRedirect(request.getContextPath() + "/Connservlet?redirectURL=" + currentURL);
            return;
        }
        
        String pathInfo = request.getPathInfo();
        
        if (pathInfo == null || pathInfo.equals("/")) {
            // Show cart page - displays all items with details
            displayCart(request, response);
        } else if (pathInfo.equals("/count")) {
            // Get cart count for AJAX requests - used for updating cart badge in UI
            getCartCount(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
    
    /**
     * Handles HTTP POST requests for cart operations.
     * Supports adding items, updating quantities, removing items, and clearing the cart.
     *
     * @param request The HTTP request object
     * @param response The HTTP response object
     * @throws ServletException If a servlet-specific error occurs
     * @throws IOException If an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        // Ensure ConnServlet has been called to initialize DAOs
        if (session.getAttribute("cartItemDAO") == null || session.getAttribute("productDAO") == null) {
            response.setContentType("application/json");
            response.getWriter().write("{\"success\": false, \"message\": \"Database connection not initialized\"}");
            return;
        }
        
        String pathInfo = request.getPathInfo();
        
        if (pathInfo == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        
        try {
            // Route the request to the appropriate handler method based on path
            switch (pathInfo) {
                case "/add":
                    // Add new item or increase quantity of existing item
                    addToCart(request, response);
                    break;
                case "/update":
                    // Update quantity of existing item
                    updateCartItem(request, response);
                    break;
                case "/remove":
                    // Remove item completely from cart
                    removeFromCart(request, response);
                    break;
                case "/clear":
                    // Clear entire cart
                    clearCart(request, response);
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                    return;
            }
        } catch (Exception e) {
            // Centralized error handling for all cart operations
            sendJSONResponse(response, false, "Error: " + e.getMessage());
        }
    }
    
    /**
     * Displays the cart page with all cart items and their details.
     * Calculates subtotals for each item and the overall cart total.
     *
     * @param request The HTTP request object
     * @param response The HTTP response object
     * @throws ServletException If a servlet-specific error occurs
     * @throws IOException If an I/O error occurs
     */
    private void displayCart(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            HttpSession session = request.getSession();
            // Get cart items with full product details
            List<Map<String, Object>> cartItemsWithDetails = getCartItemsWithDetails(request);
            request.setAttribute("cartItems", cartItemsWithDetails);
            session.setAttribute("cartItems", cartItemsWithDetails);
            
            // Calculate cart total by summing up all item subtotals
            double total = 0.0;
            for (Map<String, Object> item : cartItemsWithDetails) {
                total += (double) item.get("subtotal");
            }
            request.setAttribute("cartTotal", total);
            
            request.getRequestDispatcher("/shopping-cart.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Error retrieving cart items", e);
        }
    }
    
    /**
     * Returns the total count of items in the cart.
     * Used for AJAX requests to update the cart badge in the UI.
     *
     * @param request The HTTP request object
     * @param response The HTTP response object
     * @throws IOException If an I/O error occurs
     */
    private void getCartCount(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        HttpSession session = request.getSession();
        Map<Integer, Integer> cart = getCartFromSession(session);
        
        // Sum up quantities of all items in cart
        int count = 0;
        for (int quantity : cart.values()) {
            count += quantity;
        }
        
        response.setContentType("text/plain");
        response.getWriter().write(String.valueOf(count));
    }
    
    /**
     * Adds a product to the shopping cart or increases its quantity if already present.
     * Synchronizes with the database if user is logged in.
     *
     * @param request The HTTP request object
     * @param response The HTTP response object
     * @throws ServletException If a servlet-specific error occurs
     * @throws SQLException If a database access error occurs
     * @throws IOException If an I/O error occurs
     */
    private void addToCart(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, SQLException, IOException {
        int productId = Integer.parseInt(request.getParameter("productId"));
        int quantity = Integer.parseInt(request.getParameter("quantity"));
        
        HttpSession session = request.getSession();
        Map<Integer, Integer> cart = getCartFromSession(session);
        
        // Add to existing quantity if product already in cart
        cart.put(productId, cart.getOrDefault(productId, 0) + quantity);
        
        // Save updated cart to session
        session.setAttribute("cart", cart);
        
        // If user is logged in, sync with database
        User user = (User) session.getAttribute("user");
        if (user != null) {
            syncCartItemWithDatabase(request, user.getUserID(), productId, cart.get(productId));
        }
        
        int cartCount = getCartItemCount(cart);
        sendJSONResponse(response, true, "Product added to cart", cartCount);
    }
    
    /**
     * Updates the quantity of an item in the cart.
     * If quantity becomes zero or negative, removes the item from the cart.
     * Synchronizes with the database if user is logged in.
     *
     * @param request The HTTP request object
     * @param response The HTTP response object
     * @throws ServletException If a servlet-specific error occurs
     * @throws SQLException If a database access error occurs
     * @throws IOException If an I/O error occurs
     */
    private void updateCartItem(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, SQLException, IOException {
        int productId = Integer.parseInt(request.getParameter("productId"));
        int quantity = Integer.parseInt(request.getParameter("quantity"));
        
        HttpSession session = request.getSession();
        Map<Integer, Integer> cart = getCartFromSession(session);
        
        // Remove item if quantity is zero or negative, otherwise update quantity
        if (quantity <= 0) {
            cart.remove(productId);
        } else {
            cart.put(productId, quantity);
        }
        
        // Save updated cart to session
        session.setAttribute("cart", cart);
        
        // If user is logged in, sync with database
        User user = (User) session.getAttribute("user");
        if (user != null) {
            if (quantity <= 0) {
                deleteCartItemFromDatabase(request, user.getUserID(), productId);
            } else {
                syncCartItemWithDatabase(request, user.getUserID(), productId, quantity);
            }
        }
        
        int cartCount = getCartItemCount(cart);
        sendJSONResponse(response, true, "Cart updated", cartCount);
    }
    
    /**
     * Removes an item from the shopping cart completely.
     * Synchronizes with the database if user is logged in.
     *
     * @param request The HTTP request object
     * @param response The HTTP response object
     * @throws ServletException If a servlet-specific error occurs
     * @throws SQLException If a database access error occurs
     * @throws IOException If an I/O error occurs
     */
    private void removeFromCart(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, SQLException, IOException {
        int productId = Integer.parseInt(request.getParameter("productId"));
        
        HttpSession session = request.getSession();
        Map<Integer, Integer> cart = getCartFromSession(session);
        
        // Remove the item from cart
        cart.remove(productId);
        
        // Save updated cart to session
        session.setAttribute("cart", cart);
        
        // If user is logged in, remove from database
        User user = (User) session.getAttribute("user");
        if (user != null) {
            deleteCartItemFromDatabase(request, user.getUserID(), productId);
        }
        
        int cartCount = getCartItemCount(cart);
        sendJSONResponse(response, true, "Item removed from cart", cartCount);
    }
    
    /**
     * Clears the entire shopping cart.
     * Removes all items from both session storage and database if user is logged in.
     *
     * @param request The HTTP request object
     * @param response The HTTP response object
     * @throws ServletException If a servlet-specific error occurs
     * @throws SQLException If a database access error occurs
     * @throws IOException If an I/O error occurs
     */
    private void clearCart(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, SQLException, IOException {
        HttpSession session = request.getSession();
        session.removeAttribute("cart");
        
        // If user is logged in, clear from database
        User user = (User) session.getAttribute("user");
        if (user != null) {
            clearCartFromDatabase(request, user.getUserID());
        }
        
        sendJSONResponse(response, true, "Cart cleared", 0);
    }
    
    /**
     * Retrieves the cart from session or initializes a new one if not present.
     * The cart is stored as a Map with product IDs as keys and quantities as values.
     *
     * @param session The current HTTP session
     * @return A Map representing the shopping cart
     */
    private static Map<Integer, Integer> getCartFromSession(HttpSession session) {
        @SuppressWarnings("unchecked")
        Map<Integer, Integer> cart = (Map<Integer, Integer>) session.getAttribute("cart");
        
        if (cart == null) {
            cart = new HashMap<>();
        }
        
        return cart;
    }
    
    /**
     * Calculates the total count of items in the cart by summing all quantities.
     *
     * @param cart The cart map containing product IDs and quantities
     * @return Total number of items in cart
     */
    private int getCartItemCount(Map<Integer, Integer> cart) {
        int count = 0;
        for (int quantity : cart.values()) {
            count += quantity;
        }
        return count;
    }
    
    /**
     * Synchronizes a cart item with the database.
     * Creates a new record if the item doesn't exist, or updates quantity if it does.
     *
     * @param request The HTTP request object
     * @param userId The user ID
     * @param productId The product ID
     * @param quantity The new quantity
     * @throws SQLException If a database access error occurs
     */
    private void syncCartItemWithDatabase(HttpServletRequest request, int userId, int productId, int quantity) 
            throws SQLException {
        HttpSession session = request.getSession();
        CartItemDAO cartItemDAO = (CartItemDAO) session.getAttribute("cartItemDAO");
        
        CartItem existingItem = cartItemDAO.findByUserAndProduct(userId, productId);
        
        if (existingItem == null) {
            // Insert new item
            CartItem newItem = new CartItem(productId, userId, quantity);
            cartItemDAO.insert(newItem);
        } else {
            // Update existing item
            CartItem updatedItem = new CartItem(productId, userId, quantity);
            cartItemDAO.update(updatedItem);
        }
    }
    
    /**
     * Deletes a cart item from the database.
     *
     * @param request The HTTP request object
     * @param userId The user ID
     * @param productId The product ID
     * @throws SQLException If a database access error occurs
     */
    private void deleteCartItemFromDatabase(HttpServletRequest request, int userId, int productId) 
            throws SQLException {
        HttpSession session = request.getSession();
        CartItemDAO cartItemDAO = (CartItemDAO) session.getAttribute("cartItemDAO");
        cartItemDAO.deleteByUserAndProduct(userId, productId);
    }
    
    /**
     * Clears all cart items for a user from the database.
     *
     * @param request The HTTP request object
     * @param userId The user ID
     * @throws SQLException If a database access error occurs
     */
    private void clearCartFromDatabase(HttpServletRequest request, int userId) 
            throws SQLException {
        HttpSession session = request.getSession();
        CartItemDAO cartItemDAO = (CartItemDAO) session.getAttribute("cartItemDAO");
        cartItemDAO.deleteAllByUserId(userId);
    }
    
    /**
     * Retrieves cart items with full product details.
     * Merges the basic cart information (product IDs and quantities) with
     * complete product details from the database.
     *
     * @param request The HTTP request object
     * @return A list of maps containing product objects, quantities, and subtotals
     * @throws SQLException If a database access error occurs
     */
    private List<Map<String, Object>> getCartItemsWithDetails(HttpServletRequest request) 
            throws SQLException {
        HttpSession session = request.getSession();
        Map<Integer, Integer> cart = getCartFromSession(session);
        ProductDAO productDAO = (ProductDAO) session.getAttribute("productDAO");
        
        List<Map<String, Object>> cartItemsWithDetails = new ArrayList<>();
        
        if (!cart.isEmpty()) {
            for (Map.Entry<Integer, Integer> entry : cart.entrySet()) {
                int productId = entry.getKey();
                int quantity = entry.getValue();
                
                Product product = productDAO.findById(productId);
                if (product != null) {
                    Map<String, Object> itemDetails = new HashMap<>();
                    itemDetails.put("product", product);
                    itemDetails.put("quantity", quantity);
                    itemDetails.put("subtotal", product.getPrice() * quantity);
                    cartItemsWithDetails.add(itemDetails);
                }
            }
        }
        
        return cartItemsWithDetails;
    }
    
    /**
     * Loads cart items from database and merges with session cart.
     * Used during login to ensure cart is synchronized between guest browsing
     * and authenticated user state.
     * 
     * @param request The HTTP request object
     * @param userId The user ID
     * @throws SQLException If a database access error occurs
     */
    public static void mergeCartWithDatabase(HttpServletRequest request, int userId) 
            throws SQLException {
        HttpSession session = request.getSession();
        Map<Integer, Integer> sessionCart = getCartFromSession(session);
        CartItemDAO cartItemDAO = (CartItemDAO) session.getAttribute("cartItemDAO");
        
        // Get user's existing cart items from database
        List<CartItem> dbCartItems = cartItemDAO.getAllByUserId(userId);
        
        // Create a merged cart
        Map<Integer, Integer> mergedCart = new HashMap<>();
        
        // Add items from database
        for (CartItem item : dbCartItems) {
            mergedCart.put(item.getProductID(), item.getQuantity());
        }
        
        // Add or update items from session (session takes precedence)
        for (Map.Entry<Integer, Integer> entry : sessionCart.entrySet()) {
            mergedCart.put(entry.getKey(), entry.getValue());
            
            // Sync with database if needed
            CartItem existingItem = cartItemDAO.findByUserAndProduct(userId, entry.getKey());
            if (existingItem == null) {
                // Item exists in session but not in DB, insert it
                cartItemDAO.insert(new CartItem(entry.getKey(), userId, entry.getValue()));
            } else if (existingItem.getQuantity() != entry.getValue()) {
                // Item exists in both but with different quantity, update DB
                cartItemDAO.update(new CartItem(entry.getKey(), userId, entry.getValue()));
            }
        }
        
        // Update session with merged cart
        session.setAttribute("cart", mergedCart);
    }
    
    /**
     * Sends a JSON response with success status and message.
     * Overloaded version without cart count.
     * 
     * @param response The HTTP response object
     * @param success Whether the operation was successful
     * @param message The message to return
     * @throws IOException If an I/O error occurs
     */
    private void sendJSONResponse(HttpServletResponse response, boolean success, String message) 
            throws IOException {
        sendJSONResponse(response, success, message, -1);
    }
    
    /**
     * Sends a JSON response with success status, message, and cart count.
     * Manually constructs JSON without using external libraries.
     * 
     * @param response The HTTP response object
     * @param success Whether the operation was successful
     * @param message The message to return
     * @param cartCount The updated cart count (-1 to omit)
     * @throws IOException If an I/O error occurs
     */
    private void sendJSONResponse(HttpServletResponse response, boolean success, String message, int cartCount) 
            throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        PrintWriter out = response.getWriter();
        StringBuilder json = new StringBuilder();
        json.append("{");
        json.append("\"success\":").append(success).append(",");
        json.append("\"message\":\"").append(message).append("\"");
        
        if (cartCount >= 0) {
            json.append(",\"cartCount\":").append(cartCount);
        }
        
        json.append("}");
        out.write(json.toString());
    }
}