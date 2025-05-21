package controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.dao.UserDAO;
import model.users.Customer;
import model.users.Staff;
import model.users.User;
import utils.ValidatorUtil;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/UserCreationServlet")
public class UserCreationServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String query = request.getParameter("query");

        HttpSession session = request.getSession();
        UserDAO userDAO = (UserDAO) session.getAttribute("userDAO");
        if (userDAO == null) {
            ConnServlet.updateDAOsPOST(request, response);
            return;
        }

        String name = request.getParameter("name");
        String email = request.getParameter("email");
        if (!ValidatorUtil.isValidEmail(email)) {
            session.setAttribute("error", "Invalid email format");
            response.sendRedirect("user-management.jsp?query=" + query);
            return;
        }
        String password = request.getParameter("password");
        if (!ValidatorUtil.isValidPassword(password)) {
            session.setAttribute("error", "Password must be at least 8 characters long and contain at least one letter and one number");
            response.sendRedirect("user-management.jsp?query=" + query);
            return;
        }
        String role = request.getParameter("role");

        User user = new Customer(name, email, password);
        if (role.equalsIgnoreCase("staff")) {
            user = new Staff(user);
        }

        try {
            UserController.createUser(user, request, response);
        } catch (SQLException e) {
            if (e.getMessage().contains("UNIQUE constraint failed: User.Email")) {
                session.setAttribute("error", "Email already exists");
            } else {
                session.setAttribute("error", e.getMessage());
            }
        }
        response.sendRedirect("user-management.jsp?query=" + query);
    }
}
