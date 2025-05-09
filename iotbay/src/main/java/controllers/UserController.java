package controllers;

import jakarta.servlet.http.HttpSession;
import model.dao.AdminDAO;
import model.dao.StaffDAO;
import model.dao.UserDAO;
import model.users.Admin;
import model.users.Customer;
import model.users.Staff;
import model.users.User;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserController {
    public List<User> getAllUsers(HttpSession session) throws SQLException {
        UserDAO userDAO = (UserDAO) session.getAttribute("userDAO");
        List<User> users = new ArrayList<>();
        for (User user : userDAO.getAll()) {
            users.add(getRole(user, session));
        }
        return users;
    }

    public User getRole(User user, HttpSession session) throws SQLException {
        AdminDAO adminDAO = (AdminDAO) session.getAttribute("adminDAO");
        StaffDAO staffDAO = (StaffDAO) session.getAttribute("staffDAO");
        if (adminDAO.isAdmin(user.getUserID())) {
            return new Admin(user);
        } else if (staffDAO.isStaff(user.getUserID())) {
            return new Staff(user);
        } else {
            return new Customer(user);
        }
    }

    public void updateUser(int userId, String name, String email, String password) {
        // Logic to update an existing user
    }

    public void deleteUser(int userId) {
        // Logic to delete a user
    }

    public void setAdmin(int userId) {

    }

    public void setStaff(int userId) {
        // Logic to set a user as staff
    }
}
