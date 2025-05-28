package controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Product;
import model.dao.ProductDAO;
import model.dao.CartItemDAO;
import model.dao.OrderItemDAO;
import model.lineproducts.CartItem;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import model.IDObject;

/**
 * Controller servlet responsible for handling all product-related operations.
 * Maps to URL pattern "/products/*" and provides functionality for viewing,
 * searching, filtering, adding, updating, and deleting products.
 */
@WebServlet("/products/*")
public class ProductController extends HttpServlet {

    /**
     * Handles HTTP GET requests for product operations.
     * Supports listing all products, filtering by category, searching by name,
     * viewing product details, and accessing the inventory management page.
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

        // Get the ProductDAO from the session or initialize it if not present
        ProductDAO productDAO = (ProductDAO) session.getAttribute("productDAO");
        if (productDAO == null) {
            ConnServlet.updateDAOsGET(request, response);
            return;
        }

        try {
            // Get the pathInfo to determine what action to perform
            String pathInfo = request.getPathInfo();

            if (pathInfo == null || pathInfo.equals("/") || pathInfo.equals("/list")) {
                // Handle product listing, searching, and category filtering
                String category = request.getParameter("category");
                String searchQuery = request.getParameter("query");

                List<Product> products;

                if (searchQuery != null && !searchQuery.trim().isEmpty()) {
                    // Search by name with optional category filter
                    if (category != null && !category.trim().isEmpty()) {
                        products = productDAO.searchByNameInCategory(searchQuery, category);
                    } else {
                        products = productDAO.searchByName(searchQuery);
                    }
                } else if (category != null && !category.trim().isEmpty()) {
                    // Filter by category
                    products = productDAO.findProductsByCategory(category);
                } else {
                    // Get all products
                    products = productDAO.getAll();
                }

                // Get all categories for the sidebar
                List<String> categories = productDAO.getAllCategories();

                // Set attributes for JSP
                request.setAttribute("products", products);
                request.setAttribute("categories", categories);
                request.setAttribute("selectedCategory", category);
                request.setAttribute("searchQuery", searchQuery);

                // Forward to the index page
                request.getRequestDispatcher("/index.jsp").forward(request, response);
            } else if (pathInfo.startsWith("/detail/")) {
                // Handle product detail view
                try {
                    // Extract product ID from URL path
                    int productId = Integer.parseInt(pathInfo.substring("/detail/".length()));
                    Product product = productDAO.findById(productId);
                    if (product != null) {
                        request.setAttribute("product", product);
                        request.getRequestDispatcher("/productDetail.jsp").forward(request, response);
                    } else {
                        // Set error message and forward to product error page
                        request.setAttribute("errorMessage", "Product with ID " + productId + " not found");
                        request.setAttribute("errorTitle", "Product Not Found");
                        request.getRequestDispatcher("/productError.jsp").forward(request, response);
                    }
                } catch (NumberFormatException e) {
                    // Handle invalid product ID format
                    request.setAttribute("errorMessage", "Invalid product ID format");
                    request.setAttribute("errorTitle", "Invalid Product ID");
                    request.getRequestDispatcher("/productError.jsp").forward(request, response);
                }
            } else if (pathInfo.equals("/inventory")) {
                // Handle inventory management page
                try {
                    // Get all products for inventory management
                    List<Product> products = productDAO.getAll();
                    List<String> categories = productDAO.getAllCategories();
                    
                    // Set attributes for JSP
                    request.setAttribute("products", products);
                    request.setAttribute("categories", categories);
                    
                    // Check for any message from POST operations
                    String message = request.getParameter("message");
                    String error = request.getParameter("error");
                    if (message != null) {
                        request.setAttribute("message", message);
                    }
                    if (error != null) {
                        request.setAttribute("error", error);
                    }
                    
                    // Forward to inventory page
                    request.getRequestDispatcher("/inventory.jsp").forward(request, response);
                } catch (SQLException e) {
                    request.setAttribute("error", "Error loading products: " + e.getMessage());
                    request.getRequestDispatcher("/inventory.jsp").forward(request, response);
                }
            } else {
                // Handle unrecognized paths
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }

        } catch (SQLException e) {
            throw new ServletException("Database error", e);
        }
    }
    
    /**
     * Handles HTTP POST requests for product management operations.
     * Supports adding, updating, and deleting products.
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
        
        // Get the ProductDAO from the session or initialize it if not present
        ProductDAO productDAO = (ProductDAO) session.getAttribute("productDAO");
        
        if (productDAO == null) {
            ConnServlet.updateDAOsGET(request, response);
            return;
        }
        
        // Determine which action to perform based on the form parameter
        String action = request.getParameter("action");
        
        try {
            if ("add".equals(action)) {
                // Handle adding a new product
                String name = request.getParameter("name");
                String description = request.getParameter("description");
                double price = Double.parseDouble(request.getParameter("price"));
                int stock = Integer.parseInt(request.getParameter("stock"));
                String category = request.getParameter("category");
                
                // Handle new category creation
                if ("new_category".equals(category)) {
                    category = request.getParameter("newCategory");
                    if (category == null || category.trim().isEmpty()) {
                        category = "Uncategorized";
                    }
                }
                
                // Parse release date or use default
                Date releaseDate = parseDate(request.getParameter("releaseDate"));
                
                // Create product - IDObject constructor will call randomizeID()
                Product product = new Product(name, description, price, stock, releaseDate, category);
                
                // Use the static insert method from IDObject which handles ID generation
                IDObject.insert(productDAO, product);
                
                // Redirect back to inventory page with success message
                response.sendRedirect(request.getContextPath() + "/products/inventory?message=Product+added+successfully");
            } else if ("update".equals(action)) {
                // Handle updating an existing product
                int id = Integer.parseInt(request.getParameter("productId"));
                String name = request.getParameter("name");
                String description = request.getParameter("description");
                double price = Double.parseDouble(request.getParameter("price"));
                int stock = Integer.parseInt(request.getParameter("stock"));
                String category = request.getParameter("category");
                
                // Handle new category creation
                if ("new_category".equals(category)) {
                    category = request.getParameter("newCategory");
                    if (category == null || category.trim().isEmpty()) {
                        category = "Uncategorized";
                    }
                }
                
                // Parse release date or use default
                Date releaseDate = parseDate(request.getParameter("releaseDate"));
                
                // Create and update product
                Product product = new Product(id, name, description, price, stock, releaseDate, category);
                productDAO.update(product);
                
                // Redirect back to inventory page with success message
                response.sendRedirect(request.getContextPath() + "/products/inventory?message=Product+updated+successfully");
            } else if ("delete".equals(action)) {
                // Handle deleting a product
                int id = Integer.parseInt(request.getParameter("productId"));
                
                // Check if product is referenced in cart items
                CartItemDAO cartItemDAO = (CartItemDAO) session.getAttribute("cartItemDAO");
                List<CartItem> cartItems = cartItemDAO.getAllByProductId(id);
                
                // Check if product is referenced in order items
                OrderItemDAO orderItemDAO = (OrderItemDAO) session.getAttribute("orderItemDAO");
                boolean isInOrder = orderItemDAO.isProductInOrder(id);
                
                if (!cartItems.isEmpty()) {
                    // Product is in someone's cart, prevent deletion
                    response.sendRedirect(request.getContextPath() + 
                        "/products/inventory?error=Cannot+delete+product+because+it+is+in+active+shopping+carts");
                } else if (isInOrder) {
                    // Product is in completed orders, prevent deletion
                    response.sendRedirect(request.getContextPath() + 
                        "/products/inventory?error=Cannot+delete+product+because+it+appears+in+orders");
                } else {
                    // Safe to delete the product
                    productDAO.deleteById(id);
                    response.sendRedirect(request.getContextPath() + 
                        "/products/inventory?message=Product+deleted+successfully");
                }
            } else {
                // Handle invalid action
                response.sendRedirect(request.getContextPath() + "/products/inventory?error=Invalid+action");
            }
        } catch (SQLException | NumberFormatException e) {
            // Handle database or parsing errors
            response.sendRedirect(request.getContextPath() + "/products/inventory?error=" + e.getMessage());
        } catch (ParseException e) {
            // Handle date parsing errors
            response.sendRedirect(request.getContextPath() + "/products/inventory?error=Invalid+date+format");
        }
    }
    
    /**
     * Helper method to parse a date string into a Date object.
     * If the date string is empty or invalid, returns the current date as default.
     *
     * @param dateStr The date string in "yyyy-MM-dd" format
     * @return A Date object representing the parsed date or current date if invalid
     * @throws ParseException If the date format is invalid
     */
    private Date parseDate(String dateStr) throws ParseException {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return new Date(); // Default to current date
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.parse(dateStr);
    }
}
