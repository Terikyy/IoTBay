package controllers;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.IDObject;
import model.dao.UserDAO;
import model.users.Customer;
import model.users.User;

import java.io.IOException;

@WebServlet("/RegistrationController")
public class RegistrationServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        HttpSession session = request.getSession();
        UserDAO userDAO = (UserDAO) session.getAttribute("userDAO");

        User user = new Customer(name, email, password);

        try {
            IDObject.insert(userDAO, user);
            session.setAttribute("user", user);
            response.sendRedirect("welcome.jsp");
        } catch (Exception e) {
            if (e.getMessage().contains("UNIQUE constraint failed: User.Email"))
                session.setAttribute("error", "Email already exists");
            else
                session.setAttribute("error", e.getMessage());
            response.sendRedirect("register.jsp");
        }
    }
}
