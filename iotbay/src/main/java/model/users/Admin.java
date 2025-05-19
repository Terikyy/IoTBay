package model.users;

import jakarta.servlet.http.HttpSession;

import java.sql.SQLException;

public class Admin extends User {

    public Admin(String name, String email, String password) {
        super(name, email, password);
    }

    public Admin(int userId, String name, String email, String password) {
        super(userId, name, email, password);
    }

    public Admin(User user) {
        super(user.getUserID(), user.getName(), user.getEmail(), user.getPassword());
    }

    public Customer setCustomer(HttpSession session) throws SQLException {
        throw new UnsupportedOperationException("Cannot convert Admin to Customer");
    }

    public Staff setStaff(HttpSession session) throws SQLException {
        throw new UnsupportedOperationException("Cannot convert Admin to Staff");
    }
}