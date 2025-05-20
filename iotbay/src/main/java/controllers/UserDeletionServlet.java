package controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/UserDeletionServlet")
public class UserDeletionServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String query = request.getParameter("query");

        String userId = request.getParameter("userId");


        try {
            UserController.deleteUser(Integer.parseInt(userId), request, response);
        } catch (SQLException e) {
            throw new ServletException("Error deleting user", e);
        }

        response.sendRedirect("user-management.jsp?query=" + query);
    }
}
