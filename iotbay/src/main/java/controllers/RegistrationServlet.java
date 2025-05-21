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
import utils.ValidatorUtil;

import java.io.IOException;

@WebServlet("/RegistrationController")
public class RegistrationServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();

        String name = request.getParameter("name");
        String email = request.getParameter("email");
        if (!ValidatorUtil.isValidEmail(email)) {
            session.setAttribute("error", "Invalid email format");
            response.sendRedirect("register.jsp");
            return;
        }
        String password = request.getParameter("password");
        if (!ValidatorUtil.isValidPassword(password)) {
            session.setAttribute("error", "Password must be at least 8 characters long and contain at least one letter and one number");
            response.sendRedirect("register.jsp");
            return;
        }
        UserDAO userDAO = (UserDAO) session.getAttribute("userDAO");
        if (userDAO == null) {
            ConnServlet.updateDAOsPOST(request, response);
            return;
        }

        User user = new Customer(name, email, password);

        try {
            IDObject.insert(userDAO, user);
            session.setAttribute("user", user);
            LogController.createLog(request, response, "User " + user.getEmail() + " registered");
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
