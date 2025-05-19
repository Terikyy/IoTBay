package controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.users.User;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/UserCreationServlet")
public class UserCreationServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userRole = request.getParameter("userRole");
        String userId = request.getParameter("userId");

        User user = null;
        try {
            user = UserController.getUserById(Integer.parseInt(userId), request, response);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if (user == null)
            throw new ServletException("User not found");


        response.sendRedirect("user-management.jsp");
    }
}
