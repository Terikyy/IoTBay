package model.dao;

import model.users.Admin;
import model.users.Customer;
import model.users.Staff;
import model.users.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAO extends AbstractDAO<User> {
    private final StaffDAO staffDAO;
    private final AdminDAO adminDAO;

    private final String tableName = "User";
    private final String tableId = "id";

    public UserDAO(Connection conn) throws SQLException {
        super(conn);
        this.staffDAO = new StaffDAO(conn);
        this.adminDAO = new AdminDAO(conn);
    }

    @Override
    protected User mapRow(ResultSet rs) throws SQLException {
        User user = new Customer(
                rs.getInt("UserID"),
                rs.getString("Name"),
                rs.getString("Email"),
                rs.getString("Password"),
                rs.getString("PhoneNumber"),
                rs.getBoolean("Active")
        );
        int userId = user.getUserID();

        // Check if the user is in the Admin table
        if (adminDAO.isAdmin(userId)) {
            return new Admin(user);
        }

        // Check if the user is in the Staff table
        if (staffDAO.isStaff(userId)) {
            return new Staff(user);
        }

        return new Customer(user);
    }

    @Override
    public int insert(User user) throws SQLException {
        String query = "INSERT INTO User (UserID, Name, Email, Password) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, user.getUserID());
            ps.setString(2, user.getName());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getPassword());
            return ps.executeUpdate(); // Returns the number of rows affected
        }
    }

    @Override
    public int update(User user) throws SQLException {
        String query = "UPDATE User SET Name = ?, Email = ?, Password = ?, Active = ?, PhoneNumber = ? WHERE UserID = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.setBoolean(4, user.isActive());
            ps.setString(5, user.getPhoneNumber());
            ps.setInt(6, user.getUserID());
            return ps.executeUpdate(); // Returns the number of rows affected
        }
    }

    @Override
    public List<User> getAll() throws SQLException {
        return queryAllFromTable("User");
    }

    public List<User> query(String query) throws SQLException {
        List<User> results = new ArrayList<>();
        String sqlQuery = "SELECT * FROM User WHERE LOWER(name) LIKE ? OR LOWER(email) LIKE ? OR LOWER(PhoneNumber) LIKE ?";
        try (PreparedStatement ps = conn.prepareStatement(sqlQuery)) {
            ps.setString(1, "%" + query.toLowerCase() + "%");
            ps.setString(2, "%" + query.toLowerCase() + "%");
            ps.setString(3, "%" + query.toLowerCase() + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    results.add(mapRow(rs));
                }
            }
        }
        return results;
    }

    @Override
    public User findById(int id) throws SQLException {
        return queryById("User", "UserID", id);
    }

    public User findByEmail(String email) throws SQLException {
        return queryByColumnValue(tableName, "Email", email).get(0);
    }

    @Override
    public int deleteById(int id) throws SQLException {
        return deleteFromTableById("User", "UserID", id);
    }

    // Retrieve a user by email and password (for login authentication)
    public User authenticateUser(String email, String password) throws SQLException {
        String query = "SELECT * FROM User WHERE Email = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String dbPassword = rs.getString("Password");
                    if (dbPassword == null || dbPassword.equals(password)) {
                        return mapRow(rs);
                    }
                    return null; // Password does not match
                }
            }
        }
        return null; // User not found
    }
}
