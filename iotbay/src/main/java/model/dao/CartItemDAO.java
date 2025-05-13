package model.dao;

import model.lineproducts.CartItem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class CartItemDAO extends AbstractDAO<CartItem> {
    
    private static final String TABLE_NAME = "CartItems";

    public CartItemDAO(Connection conn) throws SQLException {
        super(conn);
    }

    @Override
    protected CartItem mapRow(ResultSet rs) throws SQLException {
        return new CartItem(
                rs.getInt("ProductID"),
                rs.getObject("UserID") != null ? rs.getInt("UserID") : null, // Handle nullable UserID
                rs.getInt("Quantity")
        );
    }

    @Override
    public int insert(CartItem cartItem) throws SQLException {
        String sql = "INSERT INTO " + TABLE_NAME + " (ProductID, UserID, Quantity) VALUES (?, ?, ?)";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, cartItem.getProductID());
            
            if (cartItem.getUserId() != null) {
                stmt.setInt(2, cartItem.getUserId());
            } else {
                stmt.setNull(2, java.sql.Types.INTEGER);
            }
            
            stmt.setInt(3, cartItem.getQuantity());
            
            return stmt.executeUpdate();
        }
    }

    @Override
    public int update(CartItem cartItem) throws SQLException {
        String sql = "UPDATE " + TABLE_NAME + " SET Quantity = ? WHERE ProductID = ? AND UserID = ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, cartItem.getQuantity());
            stmt.setInt(2, cartItem.getProductID());
            
            if (cartItem.getUserId() != null) {
                stmt.setInt(3, cartItem.getUserId());
            } else {
                stmt.setNull(3, java.sql.Types.INTEGER);
            }
            
            return stmt.executeUpdate();
        }
    }

    @Override
    public List<CartItem> getAll() throws SQLException {
        return queryAllFromTable(TABLE_NAME);
    }
    
    /**
     * Get all cart items for a specific user
     * 
     * @param userId The user ID
     * @return List of cart items for the user
     * @throws SQLException If a database access error occurs
     */
    public List<CartItem> getAllByUserId(Integer userId) throws SQLException {
        return queryByColumnValue(TABLE_NAME, "UserID", userId);
    }
    
    /**
     * Get all cart items for a specific product
     * 
     * @param productId The product ID
     * @return List of cart items for the product
     * @throws SQLException If a database access error occurs
     */
    public List<CartItem> getAllByProductId(int productId) throws SQLException {
        return queryByColumnValue(TABLE_NAME, "ProductID", productId);
    }

    @Override
    public CartItem findById(int id) throws SQLException {
        throw new UnsupportedOperationException("CartItems don't have a single ID field. Use findByUserAndProduct instead.");
    }
    
    /**
     * Find a cart item by user ID and product ID
     * 
     * @param userId The user ID
     * @param productId The product ID
     * @return The cart item if found, null otherwise
     * @throws SQLException If a database access error occurs
     */
    public CartItem findByUserAndProduct(Integer userId, int productId) throws SQLException {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE UserID = ? AND ProductID = ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            if (userId != null) {
                stmt.setInt(1, userId);
            } else {
                stmt.setNull(1, java.sql.Types.INTEGER);
            }
            stmt.setInt(2, productId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                } else {
                    return null;
                }
            }
        }
    }

    @Override
    public int deleteById(int id) throws SQLException {
        throw new UnsupportedOperationException("CartItems don't have a single ID field. Use deleteByUserAndProduct instead.");
    }
    
    /**
     * Delete a cart item by user ID and product ID
     * 
     * @param userId The user ID
     * @param productId The product ID
     * @return Number of rows affected
     * @throws SQLException If a database access error occurs
     */
    public int deleteByUserAndProduct(Integer userId, int productId) throws SQLException {
        String sql = "DELETE FROM " + TABLE_NAME + " WHERE UserID = ? AND ProductID = ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            if (userId != null) {
                stmt.setInt(1, userId);
            } else {
                stmt.setNull(1, java.sql.Types.INTEGER);
            }
            stmt.setInt(2, productId);
            
            return stmt.executeUpdate();
        }
    }
    
    /**
     * Delete all cart items for a user
     * 
     * @param userId The user ID
     * @return Number of rows affected
     * @throws SQLException If a database access error occurs
     */
    public int deleteAllByUserId(Integer userId) throws SQLException {
        String sql = "DELETE FROM " + TABLE_NAME + " WHERE UserID = ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            if (userId != null) {
                stmt.setInt(1, userId);
            } else {
                stmt.setNull(1, java.sql.Types.INTEGER);
            }
            
            return stmt.executeUpdate();
        }
    }
    
    /**
     * Delete all cart items for a product
     * 
     * @param productId The product ID
     * @return Number of rows affected
     * @throws SQLException If a database access error occurs
     */
    public int deleteAllByProductId(int productId) throws SQLException {
        return deleteFromTableById(TABLE_NAME, "ProductID", productId);
    }
}
