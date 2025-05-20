package model.dao;

import model.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

/**
 * Data Access Object for Product entities.
 * Handles all database operations related to products including CRUD operations
 * and specialized search/filter functionality.
 */
public class ProductDAO extends AbstractDAO<Product> {

    /**
     * Constructor that initializes the DAO with a database connection.
     *
     * @param conn The active database connection
     * @throws SQLException If a database access error occurs
     */
    public ProductDAO(Connection conn) throws SQLException {
        super(conn);
    }

    /**
     * Maps a database result set row to a Product object.
     * Includes fallback mechanisms for handling dates in different formats.
     *
     * @param rs The result set containing product data
     * @return A fully populated Product object
     * @throws SQLException If a database access error occurs
     */
    @Override
    protected Product mapRow(ResultSet rs) throws SQLException {
        java.util.Date releaseDate = null;
        
        // Safely handle date parsing with multiple fallback strategies
        try {
            // First try standard getDate method
            releaseDate = rs.getDate("ReleaseDate");
        } catch (SQLException e) {
            // If standard method fails, try getting as string and parsing manually
            String dateStr = rs.getString("ReleaseDate");
            if (dateStr != null && !dateStr.isEmpty()) {
                try {
                    java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
                    releaseDate = sdf.parse(dateStr);
                } catch (java.text.ParseException pe) {
                    System.err.println("Failed to parse date: " + dateStr);
                    // Use current date as last resort fallback
                    releaseDate = new java.util.Date();
                }
            }
        }
        
        // Create and return a new Product instance with data from the result set
        return new Product(
                rs.getInt("ProductID"),
                rs.getString("Name"),
                rs.getString("Description"),
                rs.getDouble("Price"),
                rs.getInt("Stock"),
                releaseDate,
                rs.getString("Category")
        );
    }

    /**
     * Inserts a new product into the database.
     *
     * @param product The product to be inserted
     * @return The ID of the newly inserted product
     * @throws SQLException If a database access error occurs or the insert fails
     */
    @Override
    public int insert(Product product) throws SQLException {
        String sql = "INSERT INTO Product (ProductID, Name, Description, Price, Stock, ReleaseDate, Category) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, product.getProductID()); // Use the product's predefined ID
            ps.setString(2, product.getName());
            ps.setString(3, product.getDescription());
            ps.setDouble(4, product.getPrice());
            ps.setInt(5, product.getStock());
            ps.setDate(6, new java.sql.Date(product.getReleaseDate().getTime()));
            ps.setString(7, product.getCategory());
            
            int affectedRows = ps.executeUpdate();
            
            // Validate that the insert was successful
            if (affectedRows == 0) {
                throw new SQLException("Creating product failed, no rows affected.");
            }
            
            return product.getProductID();
        }
    }

    /**
     * Updates an existing product in the database.
     *
     * @param product The product with updated information
     * @return The number of rows affected (should be 1 if successful)
     * @throws SQLException If a database access error occurs
     */
    @Override
    public int update(Product product) throws SQLException {
        String sql = "UPDATE Product SET Name = ?, Description = ?, Price = ?, Stock = ?, ReleaseDate = ?, Category = ? " +
                     "WHERE ProductID = ?";
        
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, product.getName());
            ps.setString(2, product.getDescription());
            ps.setDouble(3, product.getPrice());
            ps.setInt(4, product.getStock());
            ps.setDate(5, new java.sql.Date(product.getReleaseDate().getTime()));
            ps.setString(6, product.getCategory());
            ps.setInt(7, product.getProductID());
            
            return ps.executeUpdate();
        }
    }

    /**
     * Retrieves all products from the database.
     *
     * @return A list containing all products
     * @throws SQLException If a database access error occurs
     */
    @Override
    public List<Product> getAll() throws SQLException {
        // Uses the helper method from AbstractDAO
        return queryAllFromTable("Product");
    }

    /**
     * Finds a product by its unique identifier.
     *
     * @param id The product ID to search for
     * @return The matching product or null if not found
     * @throws SQLException If a database access error occurs
     */
    @Override
    public Product findById(int id) throws SQLException {
        // Uses the helper method from AbstractDAO
        return queryById("Product", "ProductID", id);
    }

    /**
     * Deletes a product from the database by its ID.
     *
     * @param id The ID of the product to delete
     * @return The number of rows affected (should be 1 if successful)
     * @throws SQLException If a database access error occurs
     */
    @Override
    public int deleteById(int id) throws SQLException {
        // Uses the helper method from AbstractDAO
        return deleteFromTableById("Product", "ProductID", id);
    }
    
    /**
     * Searches for products by name using partial matching.
     *
     * @param name The search term to match against product names
     * @return A list of products whose names contain the search term
     * @throws SQLException If a database access error occurs
     */
    public List<Product> searchByName(String name) throws SQLException {
        List<Product> results = new ArrayList<>();
        String sql = "SELECT * FROM Product WHERE Name LIKE ?";
        
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + name + "%");  // Use SQL LIKE wildcards for partial matching
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    results.add(mapRow(rs));
                }
            }
        }
        
        return results;
    }
    
    /**
     * Updates the stock quantity for a specific product.
     *
     * @param productId The ID of the product to update
     * @param newStock The new stock quantity
     * @return The number of rows affected (should be 1 if successful)
     * @throws SQLException If a database access error occurs
     */
    public int updateStock(int productId, int newStock) throws SQLException {
        String sql = "UPDATE Product SET Stock = ? WHERE ProductID = ?";
        
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, newStock);
            ps.setInt(2, productId);
            
            return ps.executeUpdate();
        }
    }
    
    /**
     * Retrieves all unique product categories from the database.
     * Empty or null categories are excluded from the results.
     *
     * @return A list of distinct category names
     * @throws SQLException If a database access error occurs
     */
    public List<String> getAllCategories() throws SQLException {
        Set<String> categories = new HashSet<>();  // Using Set to ensure uniqueness
        String sql = "SELECT DISTINCT Category FROM Product WHERE Category IS NOT NULL AND Category != ''";
        
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                categories.add(rs.getString("Category"));
            }
        }
        
        return new ArrayList<>(categories);
    }
    
    /**
     * Finds all products belonging to a specific category.
     *
     * @param category The category to filter by
     * @return A list of products in the specified category
     * @throws SQLException If a database access error occurs
     */
    public List<Product> findProductsByCategory(String category) throws SQLException {
        return queryByColumnValue("Product", "Category", category);
    }
    
    /**
     * Retrieves all products sorted first by category and then by name.
     * Useful for displaying products grouped by category.
     *
     * @return A sorted list of all products
     * @throws SQLException If a database access error occurs
     */
    public List<Product> getAllProductsSortedByCategory() throws SQLException {
        List<Product> results = new ArrayList<>();
        String sql = "SELECT * FROM Product ORDER BY Category, Name";
        
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                results.add(mapRow(rs));
            }
        }
        
        return results;
    }
    
    /**
     * Updates the category of a specific product.
     *
     * @param productId The ID of the product to update
     * @param category The new category
     * @return The number of rows affected (should be 1 if successful)
     * @throws SQLException If a database access error occurs
     */
    public int updateProductCategory(int productId, String category) throws SQLException {
        String sql = "UPDATE Product SET Category = ? WHERE ProductID = ?";
        
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, category);
            ps.setInt(2, productId);
            
            return ps.executeUpdate();
        }
    }
    
    /**
     * Searches for products by name within a specific category.
     * Allows for more targeted searching when browsing by category.
     *
     * @param name The search term to match against product names
     * @param category The category to restrict the search to
     * @return A list of matching products in the specified category
     * @throws SQLException If a database access error occurs
     */
    public List<Product> searchByNameInCategory(String name, String category) throws SQLException {
        List<Product> results = new ArrayList<>();
        String sql = "SELECT * FROM Product WHERE Name LIKE ? AND Category = ?";
        
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + name + "%");
            ps.setString(2, category);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    results.add(mapRow(rs));
                }
            }
        }
        
        return results;
    }
}
