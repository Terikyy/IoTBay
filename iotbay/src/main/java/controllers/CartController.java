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

@WebServlet("/cart/*")
public class CartController extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        // Ensure ConnServlet has been called to initialize DAOs
        if (session.getAttribute("cartItemDAO") == null || session.getAttribute("productDAO") == null) {
            // Pass the current request URL as a parameter
            String currentURL = request.getRequestURI();
            if (request.getQueryString() != null) {
                currentURL += "?" + request.getQueryString();
            }
            response.sendRedirect(request.getContextPath() + "/Connservlet?redirectURL=" + currentURL);
            return;
        }
        
        String pathInfo = request.getPathInfo();
        
        if (pathInfo == null || pathInfo.equals("/")) {
            // Show cart page
            displayCart(request, response);
        } else if (pathInfo.equals("/count")) {
            // Get cart count for AJAX requests
            getCartCount(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
    
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
            switch (pathInfo) {
                case "/add":
                    addToCart(request, response);
                    break;
                case "/update":
                    updateCartItem(request, response);
                    break;
                case "/remove":
                    removeFromCart(request, response);
                    break;
                case "/clear":
                    clearCart(request, response);
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                    return;
            }
        } catch (Exception e) {
            sendJSONResponse(response, false, "Error: " + e.getMessage());
        }
    }
    
    private void displayCart(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            List<Map<String, Object>> cartItemsWithDetails = getCartItemsWithDetails(request);
            request.setAttribute("cartItems", cartItemsWithDetails);
            
            // Calculate cart total
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
    
    private void getCartCount(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        HttpSession session = request.getSession();
        Map<Integer, Integer> cart = getCartFromSession(session);
        
        int count = 0;
        for (int quantity : cart.values()) {
            count += quantity;
        }
        
        response.setContentType("text/plain");
        response.getWriter().write(String.valueOf(count));
    }
    
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
    
    private void updateCartItem(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, SQLException, IOException {
        int productId = Integer.parseInt(request.getParameter("productId"));
        int quantity = Integer.parseInt(request.getParameter("quantity"));
        
        HttpSession session = request.getSession();
        Map<Integer, Integer> cart = getCartFromSession(session);
        
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
    
    private void removeFromCart(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, SQLException, IOException {
        int productId = Integer.parseInt(request.getParameter("productId"));
        
        HttpSession session = request.getSession();
        Map<Integer, Integer> cart = getCartFromSession(session);
        
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
    
    private static Map<Integer, Integer> getCartFromSession(HttpSession session) {
        @SuppressWarnings("unchecked")
        Map<Integer, Integer> cart = (Map<Integer, Integer>) session.getAttribute("cart");
        
        if (cart == null) {
            cart = new HashMap<>();
        }
        
        return cart;
    }
    
    private int getCartItemCount(Map<Integer, Integer> cart) {
        int count = 0;
        for (int quantity : cart.values()) {
            count += quantity;
        }
        return count;
    }
    
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
    
    private void deleteCartItemFromDatabase(HttpServletRequest request, int userId, int productId) 
            throws SQLException {
        HttpSession session = request.getSession();
        CartItemDAO cartItemDAO = (CartItemDAO) session.getAttribute("cartItemDAO");
        cartItemDAO.deleteByUserAndProduct(userId, productId);
    }
    
    private void clearCartFromDatabase(HttpServletRequest request, int userId) 
            throws SQLException {
        HttpSession session = request.getSession();
        CartItemDAO cartItemDAO = (CartItemDAO) session.getAttribute("cartItemDAO");
        cartItemDAO.deleteAllByUserId(userId);
    }
    
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
     * Loads cart items from database and merges with session cart
     * Call this method during login to ensure cart is synchronized
     */
    public static void mergeCartWithDatabase(HttpServletRequest request, int userId) 
            throws SQLException {
        HttpSession session = request.getSession();
        Map<Integer, Integer> sessionCart = getCartFromSession(session);
        CartItemDAO cartItemDAO = (CartItemDAO) session.getAttribute("cartItemDAO");
        
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
                cartItemDAO.insert(new CartItem(entry.getKey(), userId, entry.getValue()));
            } else if (existingItem.getQuantity() != entry.getValue()) {
                cartItemDAO.update(new CartItem(entry.getKey(), userId, entry.getValue()));
            }
        }
        
        // Update session with merged cart
        session.setAttribute("cart", mergedCart);
    }
    
    // Simple JSON response writer without using libraries
    private void sendJSONResponse(HttpServletResponse response, boolean success, String message) 
            throws IOException {
        sendJSONResponse(response, success, message, -1);
    }
    
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