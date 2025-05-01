package model.dao;

import model.users.Admin;
import model.users.Customer;
import model.users.Staff;
import model.users.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UserDAO extends AbstractDAO<User> {

    private final StaffDAO staffDAO;
    private final AdminDAO adminDAO;

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
                rs.getString("Password")
        );
        int userId = user.getUserID();

        // Check if the user is in the Admin table
        if (adminDAO.getById(userId)) {
            return new Admin(user);
        }

        // Check if the user is in the Staff table
        if (staffDAO.getById(userId)) {
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
        String query = "UPDATE User SET Name = ?, Email = ?, Password = ? WHERE UserID = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.setInt(4, user.getUserID());
            return ps.executeUpdate(); // Returns the number of rows affected
        }
    }

    @Override
    public List<User> get() throws SQLException {
        return getFromTable("User");
    }

    @Override
    public User getById(int id) throws SQLException {
        return getFromTableById("User", "UserID", id);
    }

    @Override
    public int delete(int id) throws SQLException {
        return deleteFromTable("User", "UserID", id);
    }

    // Retrieve a user by email and password (for login authentication)
    public User authenticate(String email, String password) throws SQLException {
        String query = "SELECT * FROM User WHERE Email = ? AND Password = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
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
}
