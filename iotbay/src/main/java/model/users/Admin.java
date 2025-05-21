package model.users;

import jakarta.servlet.http.HttpSession;

import java.sql.SQLException;

public class Admin extends User {

    public Admin(String name, String email, String password) {
        super(name, email, password);
    }

    public Admin(int userId, String name, String email, String password, String phoneNumber, boolean active) {
        super(userId, name, email, password, phoneNumber, active);
    }

    public Admin(User user) {
        this(user.getUserID(), user.getName(), user.getEmail(), user.getPassword(), user.getPhoneNumber(), user.isActive());
    }

    public Customer setCustomer(HttpSession session) throws SQLException {
        throw new UnsupportedOperationException("Cannot convert Admin to Customer");
    }

    public Staff setStaff(HttpSession session) throws SQLException {
        throw new UnsupportedOperationException("Cannot convert Admin to Staff");
    }
}