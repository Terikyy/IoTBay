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
        if (userDAO == null) {
            ConnServlet.updateDAOsGET(request, response);
            return;
        }

        try {
            User user = UserController.getRoleSpecificUser(userDAO.authenticateUser(email, password),
                    (AdminDAO) session.getAttribute("adminDAO"),
                    (StaffDAO) session.getAttribute("staffDAO"));

            if (user == null) {
                session.setAttribute("error", "Email / Password mismatch");
                response.sendRedirect("login.jsp");
                return;
            }

            if (!user.isActive()) {
                session.setAttribute("error", "This user has been deactivated. Please contact the system admin.");
                response.sendRedirect("login.jsp");
                return;
            }
            session.setAttribute("user", user);
            session.setAttribute("userID", user.getUserID()); // Added By Eric (Jiaming)

            // Add this: Merge cart data from session with database
            try {
                CartController.mergeCartWithDatabase(request, user.getUserID());
                System.out.println("Cart merged successfully for user ID: " + user.getUserID());
            } catch (Exception e) {
                System.err.println("Error merging cart: " + e.getMessage());
                // Continue login process even if cart merge fails
            }

            LogController.createLog(request, response, "User logged in", user.getUserID());
            response.sendRedirect("welcome.jsp");
        } catch (Exception e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
            session.setAttribute("error", e.getMessage());
            response.sendRedirect("login.jsp");
        }
    }
}
