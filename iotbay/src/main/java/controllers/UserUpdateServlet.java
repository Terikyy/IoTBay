package controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.users.Customer;
import model.users.Staff;
import model.users.User;
import utils.ValidatorUtil;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/UserUpdateServlet")
public class UserUpdateServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String query = request.getParameter("query");

        HttpSession session = request.getSession();

        int userId = Integer.parseInt(request.getParameter("userId"));
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        if (!ValidatorUtil.isValidEmail(email)) {
            session.setAttribute("update-error", "Invalid email format");
            response.sendRedirect("user-management.jsp?query=" + query);
            return;
        }
        String role = request.getParameter("role");
        String password = request.getParameter("password");
        boolean active = Boolean.parseBoolean(request.getParameter("active"));

        User user = new Customer(userId, name, email, password, active);
        if (role.equalsIgnoreCase("staff")) {
            user = new Staff(user);
        }

        try {
            UserController.updateUser(user, request, response);
        } catch (SQLException e) {
            if (e.getMessage().contains("UNIQUE constraint failed: User.Email")) {
                session.setAttribute("update-error", "Email already exists");
            } else {
                session.setAttribute("update-error", e.getMessage());
            }
        }

        response.sendRedirect("user-management.jsp?query=" + query);
    }
}
