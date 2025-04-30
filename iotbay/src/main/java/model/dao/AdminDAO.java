package model.dao;

import java.sql.*;

public class AdminDAO {

    private Connection conn;

    public AdminDAO(Connection conn) {
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
    public int insertAdmin(int userId) throws SQLException {
        String query = "INSERT INTO Admin (UserID) VALUES (?)";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, userId);
            return ps.executeUpdate(); // Returns the number of rows affected
        }
    }
}