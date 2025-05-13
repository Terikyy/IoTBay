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
import java.util.List;

@WebServlet("/products/*")
public class ProductController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        ProductDAO productDAO = (ProductDAO) session.getAttribute("productDAO");
        if (productDAO == null) {
            ConnServlet.updateDAOs(request, response);
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
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }

        } catch (SQLException e) {
            throw new ServletException("Database error", e);
        }
    }
}
