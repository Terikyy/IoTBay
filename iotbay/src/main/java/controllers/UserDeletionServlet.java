package controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.dao.UserDAO;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/UserDeletionServlet")
public class UserDeletionServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userId = request.getParameter("userId");
        HttpSession session = request.getSession();
        UserDAO userDAO = (UserDAO) session.getAttribute("userDAO");
        if (userDAO == null) {
            ConnServlet.updateDAOsGET(request, response);
            return;
        }

        try {
            userDAO.deleteById(Integer.parseInt(userId));
            response.sendRedirect("user-management.jsp");
        } catch (SQLException e) {
            throw new ServletException("Error deleting user", e);
        }
    }
}
