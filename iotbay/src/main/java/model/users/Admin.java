package model.users;

import jakarta.servlet.http.HttpSession;
import model.dao.AdminDAO;
import model.dao.StaffDAO;

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
        AdminDAO adminDAO = (AdminDAO) session.getAttribute("adminDAO");
        adminDAO.delete(this.getUserID());
        return new Customer(this);
    }

    public Staff setStaff(HttpSession session) throws SQLException {
        StaffDAO staffDAO = (StaffDAO) session.getAttribute("staffDAO");
        AdminDAO adminDAO = (AdminDAO) session.getAttribute("adminDAO");
        adminDAO.delete(this.getUserID());
        staffDAO.insert(this.getUserID());
        return new Staff(this);
    }

    public Admin setAdmin(HttpSession session) throws SQLException {
        throw new UnsupportedOperationException("This person is already an admin");
    }
}