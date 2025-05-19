package controllers;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.dao.AdminDAO;
import model.dao.StaffDAO;
import model.dao.UserDAO;
import model.users.Admin;
import model.users.Customer;
import model.users.Staff;
import model.users.User;

@WebServlet("/UserController")
public class UserController extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        UserDAO userDAO = (UserDAO) session.getAttribute("userDAO");
        if (userDAO == null) {
            ConnServlet.updateDAOsPOST(request, response);
            return;
        }

        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String streetName = request.getParameter("streetName");
        String streetNumber = request.getParameter("streetNumber");
        String suburb = request.getParameter("suburb");
        String postcode = request.getParameter("postalCode");
        String city = request.getParameter("city");
        String state = request.getParameter("state");
    }

    public static List<User> getAllUsers(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        HttpSession session = request.getSession();
        UserDAO userDAO = (UserDAO) session.getAttribute("userDAO");
        if (userDAO == null) {
            ConnServlet.updateDAOsGET(request, response);
            return null;
        }
        List<User> users = new ArrayList<>();
        for (User user : userDAO.getAll()) {
            users.add(getRoleSpecificUser(user,
                    (AdminDAO) session.getAttribute("adminDAO"),
                    (StaffDAO) session.getAttribute("staffDAO")));
        }
        return users;
    }

    public static User getUserById(int userId, HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        HttpSession session = request.getSession();
        UserDAO userDAO = (UserDAO) session.getAttribute("userDAO");
        if (userDAO == null) {
            ConnServlet.updateDAOsGET(request, response);
            return null;
        }
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
}
