package model.dao;

import java.sql.*;
import java.util.List;
import model.users.*;

public class UserDAO extends AbstractDAO<User> {

    private StaffDAO staffDAO;
    private AdminDAO adminDAO;

    public UserDAO(Connection conn) throws SQLException {
        super(conn);
        this.staffDAO = new StaffDAO(conn);
        this.adminDAO = new AdminDAO(conn);
    }

    @Override
    protected User mapRow(ResultSet rs) throws SQLException {
        // Default to Customer, but determine the actual type later
        return new Customer(
            rs.getInt("UserID"),
            rs.getString("Name"),
            rs.getString("Email"),
            rs.getString("Password")
        );
    }

    @Override
    public int insert(User user) throws SQLException {
        String query = "INSERT INTO User (UserID, Name, Email, Password) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = st.getConnection().prepareStatement(query)) {
            ps.setInt(1, user.getUserID());
            ps.setString(2, user.getName());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getPassword());
            return ps.executeUpdate(); // Returns the number of rows affected
        }
    }

    @Override
    public int update(User user) throws SQLException {
        String query = "UPDATE User SET Name = ?, Email = ?, Password = ? WHERE UserID = ?";
        try (PreparedStatement ps = st.getConnection().prepareStatement(query)) {
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.setInt(4, user.getUserID());
            return ps.executeUpdate(); // Returns the number of rows affected
        }
    }

    // Retrieve a user by email and password (for login authentication)
    public User authenticate(String email, String password) throws SQLException {
        String query = "SELECT * FROM User WHERE Email = ? AND Password = ?";
        try (PreparedStatement ps = st.getConnection().prepareStatement(query)) {
            ps.setString(1, email);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        }
        return null; // User not found
    }

    // Retrieve all users with a specific name
    public List<User> getUsersByName(String name) throws SQLException {
        return getByColumn("User", "Name", name);
    }

    // Determine the user type and instantiate the appropriate subclass
    public User getUserById(int userId) throws SQLException {
        String query = "SELECT * FROM User WHERE UserID = ?";
        try (PreparedStatement ps = st.getConnection().prepareStatement(query)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // Check if the user is in the Admin table
                    if (adminDAO.isAdmin(userId)) {
                        return new Admin(
                            rs.getInt("UserID"),
                            rs.getString("Name"),
                            rs.getString("Email"),
                            rs.getString("Password")
                        );
                    }

                    // Check if the user is in the Staff table
                    if (staffDAO.isStaff(userId)) {
                        return new Staff(
                            rs.getInt("UserID"),
                            rs.getString("Name"),
                            rs.getString("Email"),
                            rs.getString("Password")
                        );
                    }

                    // Default to Customer if not found in Admin or Staff
                    return new Customer(
                        rs.getInt("UserID"),
                        rs.getString("Name"),
                        rs.getString("Email"),
                        rs.getString("Password")
                    );
                }
            }
        }
        return null; // User not found
    }
}
