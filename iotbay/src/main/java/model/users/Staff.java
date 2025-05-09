package model.users;

import jakarta.servlet.http.HttpSession;
import model.dao.AdminDAO;
import model.dao.StaffDAO;

import java.sql.SQLException;

public class Staff extends User {

    public Staff(String name, String email, String password) {
        super(name, email, password);
    }

    public Staff(int userId, String name, String email, String password) {
        super(userId, name, email, password);
    }

    public Staff(User user) {
        super(user.getUserID(), user.getName(), user.getEmail(), user.getPassword());
    }

    public Customer setCustomer(HttpSession session) throws SQLException {
        StaffDAO staffDAO = (StaffDAO) session.getAttribute("staffDAO");
        staffDAO.delete(this.getUserID());
        return new Customer(this);
    }

    public Admin setAdmin(HttpSession session) throws SQLException {
        StaffDAO staffDAO = (StaffDAO) session.getAttribute("staffDAO");
        AdminDAO adminDAO = (AdminDAO) session.getAttribute("adminDAO");
        staffDAO.delete(this.getUserID());
        adminDAO.insert(this.getUserID());
        return new Admin(this);
    }

    @Override
    public Staff setStaff(HttpSession session) throws SQLException {
        throw new UnsupportedOperationException("This person is already a staff member");
    }
}