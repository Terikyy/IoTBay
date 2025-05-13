package model.dao;

import model.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

public class ProductDAO extends AbstractDAO<Product> {

    public ProductDAO(Connection conn) throws SQLException {
        super(conn);
    }

    @Override
    protected Product mapRow(ResultSet rs) throws SQLException {
        return new Product(
                rs.getInt("ProductID"),
                rs.getString("Name"),
                rs.getString("Description"),
                rs.getDouble("Price"),
                rs.getInt("Stock"),
                rs.getDate("ReleaseDate"),
                rs.getString("Category")
        );
    }

    @Override
    public int insert(Product product) throws SQLException {
        String sql = "INSERT INTO Product (Name, Description, Price, Stock, ReleaseDate, Category) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, product.getName());
            ps.setString(2, product.getDescription());
            ps.setDouble(3, product.getPrice());
            ps.setInt(4, product.getStock());
            ps.setDate(5, new java.sql.Date(product.getReleaseDate().getTime()));
            ps.setString(6, product.getCategory());
            
            int affectedRows = ps.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Creating product failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating product failed, no ID obtained.");
                }
            }
        }
    }

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

    @Override
    public List<Product> getAll() throws SQLException {
        // Using the helper method from AbstractDAO
        return queryAllFromTable("Product");
    }

    @Override
    public Product findById(int id) throws SQLException {
        // Using the helper method from AbstractDAO
        return queryById("Product", "ProductID", id);
    }

    @Override
    public int deleteById(int id) throws SQLException {
        // Using the helper method from AbstractDAO
        return deleteFromTableById("Product", "ProductID", id);
    }
    
    // Search products by name (partial match)
    public List<Product> searchByName(String name) throws SQLException {
        List<Product> results = new ArrayList<>();
        String sql = "SELECT * FROM Product WHERE Name LIKE ?";
        
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + name + "%");
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    results.add(mapRow(rs));
                }
            }
        }
        
        return results;
    }
    
    // Update stock quantity
    public int updateStock(int productId, int newStock) throws SQLException {
        String sql = "UPDATE Product SET Stock = ? WHERE ProductID = ?";
        
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, newStock);
            ps.setInt(2, productId);
            
            return ps.executeUpdate();
        }
    }
    
    // Get all available categories
    public List<String> getAllCategories() throws SQLException {
        Set<String> categories = new HashSet<>();
        String sql = "SELECT DISTINCT Category FROM Product WHERE Category IS NOT NULL AND Category != ''";
        
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                categories.add(rs.getString("Category"));
            }
        }
        
        return new ArrayList<>(categories);
    }
    
    // Find products by category
    public List<Product> findProductsByCategory(String category) throws SQLException {
        return queryByColumnValue("Product", "Category", category);
    }
    
    // Get products sorted by category
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
    
    // Update product category
    public int updateProductCategory(int productId, String category) throws SQLException {
        String sql = "UPDATE Product SET Category = ? WHERE ProductID = ?";
        
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, category);
            ps.setInt(2, productId);
            
            return ps.executeUpdate();
        }
    }
    
    // Search within a category
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
