package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminDAO {
    private final Connection conn;

    public AdminDAO(Connection conn) throws SQLException {
        this.conn = conn;
    }

    // Check if a user is an admin
    public boolean isAdmin(int userId) throws SQLException {
        String query = "SELECT * FROM Admin WHERE UserID = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next(); // Returns true if a record exists
            }
        }
    }

    // Add a user to the Admin table
    public int insert(int userId) throws SQLException {
        throw new UnsupportedOperationException("System Admin should not be added!!");
    }

    public boolean delete(int userId) throws SQLException {
        throw new UnsupportedOperationException("System Admin should not be deleted!!");
    }
}