package controllers;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.dao.LogDAO;
import model.users.User;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/LogOutServlet")
public class LogOutServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        LogDAO logDAO = (LogDAO) session.getAttribute("logDAO");
        if (logDAO == null) {
            ConnServlet.updateDAOsGET(request, response);
            return;
        }

        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect("logout.jsp");
            return;
        }

        try {
            LogController.createLog(request, response, "User logged out", user.getUserID());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        session.invalidate();
        response.sendRedirect("logout.jsp");
    }

}
