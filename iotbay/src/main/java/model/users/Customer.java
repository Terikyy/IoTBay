package model.users;

import jakarta.servlet.http.HttpSession;
import model.dao.AdminDAO;
import model.dao.StaffDAO;

import java.sql.SQLException;

public class Customer extends User {

    public Customer(String name, String email, String password) {
        super(name, email, password);
    }

    public Customer(int userId, String name, String email, String password) {
        super(userId, name, email, password);
    }

    public Customer(User user) {
        super(user.getUserID(), user.getName(), user.getEmail(), user.getPassword());
    }

    public Admin setAdmin(HttpSession session) throws SQLException {
        AdminDAO adminDAO = (AdminDAO) session.getAttribute("adminDAO");
        adminDAO.insert(this.getUserID());
        return new Admin(this);
    }

    public Staff setStaff(HttpSession session) throws SQLException {
        StaffDAO staffDAO = (StaffDAO) session.getAttribute("staffDAO");
        staffDAO.insert(this.getUserID());
        return new Staff(this);
    }

    public Customer setCustomer(HttpSession session) throws SQLException {
        throw new UnsupportedOperationException("This person is already a customer");
    }
}
