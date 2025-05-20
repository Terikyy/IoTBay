package controllers;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.dao.AdminDAO;
import model.dao.StaffDAO;
import model.dao.UserDAO;
import model.users.User;

import java.io.IOException;
import java.util.Arrays;

@WebServlet("/LoginController")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        HttpSession session = request.getSession();
        UserDAO userDAO = (UserDAO) session.getAttribute("userDAO");
        System.out.println("Test 1" + email + password);
        if (userDAO == null) {
            ConnServlet.updateDAOsGET(request, response);
            return;
        }

        try {
            User user = UserController.getRoleSpecificUser(userDAO.authenticateUser(email, password),
                    (AdminDAO) session.getAttribute("adminDAO"),
                    (StaffDAO) session.getAttribute("staffDAO"));

            if (user != null) {
                session.setAttribute("user", user);
                session.setAttribute("userID", user.getId()); // Added By Eric (Jiaming)
                LogController.createLog(request, response, "User " + user.getEmail() + " logged in");
                response.sendRedirect("welcome.jsp");
            } else {
                session.setAttribute("error", "Email / Password mismatch");
                response.sendRedirect("login.jsp");
            }
        } catch (Exception e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
            session.setAttribute("error", e.getMessage());
            response.sendRedirect("login.jsp");
        }
    }
}
