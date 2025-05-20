package model.dao;

import model.lineproducts.CartItem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Data Access Object for CartItem entities.
 * Manages database operations for shopping cart items, including adding, updating,
 * retrieving, and removing items from users' shopping carts.
 */
public class CartItemDAO extends AbstractDAO<CartItem> {
    
    /**
     * Constant for the database table name to avoid hardcoding throughout the class
     */
    private static final String TABLE_NAME = "CartItem";

    /**
     * Constructor that initializes the DAO with a database connection.
     *
     * @param conn The active database connection
     * @throws SQLException If a database access error occurs
     */
    public CartItemDAO(Connection conn) throws SQLException {
        super(conn);
    }

    /**
     * Maps a database result set row to a CartItem object.
     * Handles nullable UserID for cases like guest shopping carts.
     *
     * @param rs The result set containing cart item data
     * @return A fully populated CartItem object
     * @throws SQLException If a database access error occurs
     */
    @Override
    protected CartItem mapRow(ResultSet rs) throws SQLException {
        return new CartItem(
                rs.getInt("ProductID"),
                rs.getObject("UserID") != null ? rs.getInt("UserID") : null, // Handle nullable UserID
                rs.getInt("Quantity")
        );
    }

    /**
     * Inserts a new cart item into the database.
     * Handles null UserID values for guest cart functionality.
     *
     * @param cartItem The cart item to be inserted
     * @return The number of rows affected (should be 1 if successful)
     * @throws SQLException If a database access error occurs
     */
    @Override
    public int insert(CartItem cartItem) throws SQLException {
        String sql = "INSERT INTO " + TABLE_NAME + " (ProductID, UserID, Quantity) VALUES (?, ?, ?)";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, cartItem.getProductID());
            
            // Handle nullable UserID - set to NULL in database if it's null in the object
            if (cartItem.getUserId() != null) {
                stmt.setInt(2, cartItem.getUserId());
            } else {
                stmt.setNull(2, java.sql.Types.INTEGER);
            }
            
            stmt.setInt(3, cartItem.getQuantity());
            
            return stmt.executeUpdate();
        }
    }

    /**
     * Updates an existing cart item in the database.
     * Typically used to change the quantity of a product in the cart.
     *
     * @param cartItem The cart item with updated information
     * @return The number of rows affected (should be 1 if successful)
     * @throws SQLException If a database access error occurs
     */
    @Override
    public int update(CartItem cartItem) throws SQLException {
        String sql = "UPDATE " + TABLE_NAME + " SET Quantity = ? WHERE ProductID = ? AND UserID = ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, cartItem.getQuantity());
            stmt.setInt(2, cartItem.getProductID());
            
            // Handle nullable UserID in WHERE clause
            if (cartItem.getUserId() != null) {
                stmt.setInt(3, cartItem.getUserId());
            } else {
                stmt.setNull(3, java.sql.Types.INTEGER);
            }
            
            return stmt.executeUpdate();
        }
    }

    /**
     * Retrieves all cart items from the database regardless of user.
     * Note: This may return a large dataset in a production environment.
     *
     * @return A list containing all cart items
     * @throws SQLException If a database access error occurs
     */
    @Override
    public List<CartItem> getAll() throws SQLException {
        return queryAllFromTable(TABLE_NAME);
    }
    
    /**
     * Get all cart items for a specific user.
     * Represents the entire shopping cart for a user.
     * 
     * @param userId The user ID (can be null for guest carts)
     * @return List of cart items for the user
     * @throws SQLException If a database access error occurs
     */
    public List<CartItem> getAllByUserId(Integer userId) throws SQLException {
        return queryByColumnValue(TABLE_NAME, "UserID", userId);
    }
    
    /**
     * Get all cart items for a specific product across all users.
     * Useful for analyzing product popularity or updating carts when a product changes.
     * 
     * @param productId The product ID
     * @return List of cart items for the product
     * @throws SQLException If a database access error occurs
     */
    public List<CartItem> getAllByProductId(int productId) throws SQLException {
        return queryByColumnValue(TABLE_NAME, "ProductID", productId);
    }

    /**
     * This method is not supported for CartItems as they use composite primary keys.
     * Use findByUserAndProduct instead to locate specific cart items.
     *
     * @param id The ID to search for (not applicable for cart items)
     * @return Never returns as method throws exception
     * @throws UnsupportedOperationException Always thrown when this method is called
     */
    @Override
    public CartItem findById(int id) throws SQLException {
        throw new UnsupportedOperationException("CartItems don't have a single ID field. Use findByUserAndProduct instead.");
    }
    
    /**
     * Find a cart item by user ID and product ID.
     * This is the proper way to look up a specific cart item since CartItems
     * use a composite key of (UserID, ProductID).
     * 
     * @param userId The user ID (can be null for guest carts)
     * @param productId The product ID
     * @return The cart item if found, null otherwise
     * @throws SQLException If a database access error occurs
     */
    public CartItem findByUserAndProduct(Integer userId, int productId) throws SQLException {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE UserID = ? AND ProductID = ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            // Handle nullable UserID in WHERE clause
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

    /**
     * This method is not supported for CartItems as they use composite primary keys.
     * Use deleteByUserAndProduct instead to delete specific cart items.
     *
     * @param id The ID to delete (not applicable for cart items)
     * @return Never returns as method throws exception
     * @throws UnsupportedOperationException Always thrown when this method is called
     */
    @Override
    public int deleteById(int id) throws SQLException {
        throw new UnsupportedOperationException("CartItems don't have a single ID field. Use deleteByUserAndProduct instead.");
    }
    
    /**
     * Delete a cart item by user ID and product ID.
     * Used when removing a single product from a user's cart.
     * 
     * @param userId The user ID (can be null for guest carts)
     * @param productId The product ID
     * @return Number of rows affected (should be 1 if successful, 0 if item not found)
     * @throws SQLException If a database access error occurs
     */
    public int deleteByUserAndProduct(Integer userId, int productId) throws SQLException {
        String sql = "DELETE FROM " + TABLE_NAME + " WHERE UserID = ? AND ProductID = ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            // Handle nullable UserID in WHERE clause
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
     * Delete all cart items for a user.
     * Used when checking out or clearing a cart completely.
     * 
     * @param userId The user ID (can be null for guest carts)
     * @return Number of rows affected, corresponding to number of items removed
     * @throws SQLException If a database access error occurs
     */
    public int deleteAllByUserId(Integer userId) throws SQLException {
        String sql = "DELETE FROM " + TABLE_NAME + " WHERE UserID = ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            // Handle nullable UserID in WHERE clause
            if (userId != null) {
                stmt.setInt(1, userId);
            } else {
                stmt.setNull(1, java.sql.Types.INTEGER);
            }
            
            return stmt.executeUpdate();
        }
    }
    
    /**
     * Delete all cart items for a specific product across all users.
     * Typically used when a product is discontinued or removed from the catalog.
     * 
     * @param productId The product ID to remove from all carts
     * @return Number of rows affected, corresponding to number of items removed
     * @throws SQLException If a database access error occurs
     */
    public int deleteAllByProductId(int productId) throws SQLException {
        return deleteFromTableById(TABLE_NAME, "ProductID", productId);
    }
}
