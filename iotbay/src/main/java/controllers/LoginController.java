package controllers;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.dao.UserDAO;
import model.users.User;

import java.io.IOException;

@WebServlet("/LoginController")
public class LoginController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        HttpSession session = request.getSession();
        UserDAO userDAO = (UserDAO) session.getAttribute("userDAO");
        try {
            User user = userDAO.authenticateUser(email, password);
            if (user != null) {
                session.setAttribute("user", user);
                response.sendRedirect("welcome.jsp");
            } else {
                session.setAttribute("error", "Email / Password mismatch");
                response.sendRedirect("login.jsp");
            }
        } catch (Exception e) {
            session.setAttribute("error", e.getMessage());
            response.sendRedirect("login.jsp");
        }
    }
}
