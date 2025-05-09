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
    public static List<User> getAllUsers(HttpSession session) throws SQLException {
        UserDAO userDAO = (UserDAO) session.getAttribute("userDAO");
        List<User> users = new ArrayList<>();
        for (User user : userDAO.getAll()) {
            users.add(getRoleSpecificUser(user,
                    (AdminDAO) session.getAttribute("adminDAO"),
                    (StaffDAO) session.getAttribute("staffDAO")));
        }
        return users;
    }

    public static User getUserById(int userId, HttpSession session) throws SQLException {
        UserDAO userDAO = (UserDAO) session.getAttribute("userDAO");
        User user = userDAO.findById(userId);
        return getRoleSpecificUser(user,
                (AdminDAO) session.getAttribute("adminDAO"),
                (StaffDAO) session.getAttribute("staffDAO"));
    }

    public static User getRoleSpecificUser(User user, AdminDAO adminDAO, StaffDAO staffDAO) throws SQLException {
        if (user == null)
            return null;
        if (adminDAO.isAdmin(user.getUserID())) {
            return new Admin(user);
        } else if (staffDAO.isStaff(user.getUserID())) {
            return new Staff(user);
        } else {
            return new Customer(user);
        }
    }

    public static void updateUser(int userId, String name, String email, String password) {
        // Logic to update an existing user
    }

    public static void deleteUser(int userId) {
        // Logic to delete a user
    }

    public static void updateUserRole(int userId, String role) {
        if (role.equals("Admin")) {
            setAdmin(userId);
        } else if (role.equals("Staff")) {
            setStaff(userId);
        } else {
            setCustomer(userId);
        }
    }

    public static void setAdmin(int userId) {

    }

    public static void setStaff(int userId) {
        // Logic to set a user as staff
    }

    public static void setCustomer(int userId) {
        // Logic to set a user as customer
    }
}
