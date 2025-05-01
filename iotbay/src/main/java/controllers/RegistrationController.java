package controllers;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.dao.UserDAO;
import model.users.Customer;
import model.users.User;
import utils.IDUtil;

import java.io.IOException;

@WebServlet("/RegistrationController")
public class RegistrationController extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        HttpSession session = request.getSession();
        UserDAO userDAO = (UserDAO) session.getAttribute("userDAO");

        User user = new Customer(IDUtil.generateID(), name, email, password);

        try {
            userDAO.insert(user);
            session.setAttribute("user", user);
            session.removeAttribute("error");
            response.sendRedirect("welcome.jsp");
        } catch (Exception e) {
            session.setAttribute("error", e.getMessage());
            response.sendRedirect("register.jsp?error=Registration failed");
        }
    }
}
