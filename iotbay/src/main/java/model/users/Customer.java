package model.users;

import jakarta.servlet.http.HttpSession;
import model.dao.StaffDAO;

import java.sql.SQLException;

public class Customer extends User {

    public Customer(String name, String email, String password) {
        super(name, email, password);
    }

    public Customer(int userId, String name, String email, String password, boolean active) {
        super(userId, name, email, password, active);
    }

    public Customer(User user) {
        this(user.getUserID(), user.getName(), user.getEmail(), user.getPassword(), user.isActive());
    }

    public Staff setStaff(HttpSession session) throws SQLException {
        StaffDAO staffDAO = (StaffDAO) session.getAttribute("staffDAO");
        staffDAO.insert(this.getUserID());
        return new Staff(this);
    }

    public Customer setCustomer(HttpSession session) throws SQLException {
        return this;
    }
}
