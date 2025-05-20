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

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/UserUpdateServlet")
public class UserUpdateServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

        int userId = Integer.parseInt(request.getParameter("userId"));
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String role = request.getParameter("role");
        String password = request.getParameter("password");
        boolean active = Boolean.parseBoolean(request.getParameter("active"));
        String addressIDString = request.getParameter("addressID");
        Integer addressID = addressIDString == null ? null : Integer.parseInt(addressIDString);

        User user = new Customer(userId, name, email, password, active, addressID);
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

        response.sendRedirect("user-management.jsp");
    }
}
