package controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.users.User;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/UserRoleServlet")
public class UserRoleServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userRole = request.getParameter("userRole");
        String userId = request.getParameter("userId");

        User user = null;
        try {
            user = UserController.getUserById(Integer.parseInt(userId), request.getSession());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if (user == null)
            throw new ServletException("User not found");

        // Update the user role
        if (userRole.equalsIgnoreCase("Admin")) {
            try {
                user.setAdmin(request.getSession());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else if (userRole.equalsIgnoreCase("Staff")) {
            try {
                user.setStaff(request.getSession());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else if (userRole.equalsIgnoreCase("Customer")) {
            try {
                user.setCustomer(request.getSession());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            System.out.println(userRole);
            throw new ServletException("Invalid role");
        }

        response.sendRedirect("user-management.jsp");
    }
}
