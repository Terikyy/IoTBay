package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StaffDAO {

    private Connection conn;

    public StaffDAO(Connection conn) {
        this.conn = conn;
    }

    // Check if a user is a staff member
    public boolean getById(int userId) throws SQLException {
        String query = "SELECT * FROM Staff WHERE UserID = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next(); // Returns true if a record exists
            }
        }
    }

    // Add a user to the Staff table
    public int insert(int userId) throws SQLException {
        String query = "INSERT INTO Staff (UserID) VALUES (?)";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, userId);
            return ps.executeUpdate(); // Returns the number of rows affected
        }
    }

    public int delete(int userId) throws SQLException {
        String query = "DELETE FROM Staff WHERE UserID = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, userId);
            return ps.executeUpdate(); // Returns the number of rows affected
        }
    }
}