package controllers;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.dao.LogDAO;
import model.Log; 
import model.dao.AddressDAO;
import model.users.User;
import model.Address;

@WebServlet("/accessLogs")
public class AddressController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        LogDAO accessLogDAO = (LogDAO) session.getAttribute("accessLogDAO");
        if (accessLogDAO == null) {
            throw new IOException("AccessLogDAO is not initialized in the session.");
        }

        try {
            // Use LogDAO to retrieve logs for the user
            List<Log> userLogs = accessLogDAO.query("SELECT * FROM Log WHERE UserId = " + user.getUserID());
            request.setAttribute("accessLogs", userLogs);
            request.getRequestDispatcher("accessLogs.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Error retrieving access logs", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String name = request.getParameter("name");
        int streetNumber = Integer.parseInt(request.getParameter("streetNumber"));
        String streetName = request.getParameter("streetName");
        int postcode = Integer.parseInt(request.getParameter("postcode"));
        String suburb = request.getParameter("suburb");
        String city = request.getParameter("city");
        String state = request.getParameter("state");

        Address address = new Address(name, streetNumber, streetName, postcode, suburb, city, state);

        AddressDAO addressDAO = (AddressDAO) session.getAttribute("addressDAO");
        if (addressDAO == null) {
            throw new IOException("AddressDAO is not initialized in the session.");
        }

        try {
            addressDAO.insert(address);
            response.sendRedirect("addresses.jsp");
        } catch (SQLException e) {
            throw new ServletException("Error inserting address", e);
        }
    }
}



