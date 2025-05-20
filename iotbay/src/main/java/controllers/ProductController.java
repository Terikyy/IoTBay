package controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Product;
import model.dao.ProductDAO;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import model.IDObject;

@WebServlet("/products/*")
public class ProductController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        ProductDAO productDAO = (ProductDAO) session.getAttribute("productDAO");
        if (productDAO == null) {
            ConnServlet.updateDAOsGET(request, response);
            return;
        }

        try {
            // Get the pathInfo to determine what action to perform
            String pathInfo = request.getPathInfo();

            if (pathInfo == null || pathInfo.equals("/") || pathInfo.equals("/list")) {
                // Get all products
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
                // Get product detail
                try {
                    int productId = Integer.parseInt(pathInfo.substring("/detail/".length()));
                    Product product = productDAO.findById(productId);
                    if (product != null) {
                        request.setAttribute("product", product);
                        request.getRequestDispatcher("/productDetail.jsp").forward(request, response);
                    } else {
                        response.sendError(HttpServletResponse.SC_NOT_FOUND, "Product not found");
                    }
                } catch (NumberFormatException e) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid product ID");
                }
            } else if (pathInfo.equals("/inventory")) {
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
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }

        } catch (SQLException e) {
            throw new ServletException("Database error", e);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        
        ProductDAO productDAO = (ProductDAO) session.getAttribute("productDAO");
        
        if (productDAO == null) {
            ConnServlet.updateDAOsGET(request, response);
            return;
        }
        
        String action = request.getParameter("action");
        
        try {
            if ("add".equals(action)) {
                // Add a new product
                String name = request.getParameter("name");
                String description = request.getParameter("description");
                double price = Double.parseDouble(request.getParameter("price"));
                int stock = Integer.parseInt(request.getParameter("stock"));
                String category = request.getParameter("category");
                if ("new_category".equals(category)) {
                    category = request.getParameter("newCategory");
                    if (category == null || category.trim().isEmpty()) {
                        category = "Uncategorized";
                    }
                }
                Date releaseDate = parseDate(request.getParameter("releaseDate"));
                
                // Create product - IDObject constructor will call randomizeID()
                Product product = new Product(name, description, price, stock, releaseDate, category);
                
                // Use the static insert method from IDObject which handles ID generation
                IDObject.insert(productDAO, product);
                
                response.sendRedirect(request.getContextPath() + "/products/inventory?message=Product+added+successfully");
            } else if ("update".equals(action)) {
                // Update an existing product
                int id = Integer.parseInt(request.getParameter("productId"));
                String name = request.getParameter("name");
                String description = request.getParameter("description");
                double price = Double.parseDouble(request.getParameter("price"));
                int stock = Integer.parseInt(request.getParameter("stock"));
                String category = request.getParameter("category");
                if ("new_category".equals(category)) {
                    category = request.getParameter("newCategory");
                    if (category == null || category.trim().isEmpty()) {
                        category = "Uncategorized";
                    }
                }
                Date releaseDate = parseDate(request.getParameter("releaseDate"));
                
                Product product = new Product(id, name, description, price, stock, releaseDate, category);
                productDAO.update(product);
                
                response.sendRedirect(request.getContextPath() + "/products/inventory?message=Product+updated+successfully");
            } else if ("delete".equals(action)) {
                // Delete a product
                int id = Integer.parseInt(request.getParameter("productId"));
                productDAO.deleteById(id);
                
                response.sendRedirect(request.getContextPath() + "/products/inventory?message=Product+deleted+successfully");
            } else {
                response.sendRedirect(request.getContextPath() + "/products/inventory?error=Invalid+action");
            }
        } catch (SQLException | NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/products/inventory?error=" + e.getMessage());
        } catch (ParseException e) {
            response.sendRedirect(request.getContextPath() + "/products/inventory?error=Invalid+date+format");
        }
    }
    
    private Date parseDate(String dateStr) throws ParseException {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return new Date(); // Default to current date
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.parse(dateStr);
    }
}
