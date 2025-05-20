package controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.dao.UserDAO;
import model.users.Customer;
import model.users.User;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/UserUpdateServlet")
public class UserUpdateServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        UserDAO userDAO = (UserDAO) session.getAttribute("userDAO");
        if (userDAO == null) {
            ConnServlet.updateDAOsPOST(request, response);
            return;
        }

        int userId = Integer.parseInt(request.getParameter("userId"));
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String role = request.getParameter("role");
        String password = request.getParameter("password");
        boolean active = Boolean.parseBoolean(request.getParameter("active"));
        System.out.println("Active: " + request.getParameter("active"));
        String addressIDString = request.getParameter("addressID");
        Integer addressID = addressIDString == null ? null : Integer.parseInt(addressIDString);

        User user = new Customer(userId, name, email, password, active, addressID);

        try {
            User oldUser = userDAO.findById(userId);
            userDAO.update(user);
            if (oldUser.getEmail().equals(user.getEmail()))
                LogController.createLog(request, response, "Admin updated user " + oldUser.getEmail());
            else
                LogController.createLog(request, response, "Admin updated user " + oldUser.getEmail() + ". New email: " + user.getEmail());
            if (role.equalsIgnoreCase("staff")) {
                user.setStaff(request, response);
                LogController.createLog(request, response, "Admin set role of " + user.getEmail() + " to staff");
            } else {
                user.setCustomer(request, response);
                LogController.createLog(request, response, "Admin set role of " + user.getEmail() + " to staff");
            }
        } catch (SQLException e) {
            if (e.getMessage().contains("UNIQUE constraint failed: User.Email")) {
                session.setAttribute("update-error", "Email already exists");
            } else {
                session.setAttribute("update-error", e.getMessage());
            }
        }

        response.sendRedirect("user-management.jsp");
    }
}
