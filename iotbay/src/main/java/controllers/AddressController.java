package controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Address;
import model.dao.AddressDAO;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/address")
public class AddressController extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();

        String name = request.getParameter("name");
        String email = request.getParameter("email");

        AddressDAO addressDAO = (AddressDAO) session.getAttribute("addressDAO");


    }

    // Insert Address method
    private void insertAddress(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String name = request.getParameter("name");
        int streetNumber = Integer.parseInt(request.getParameter("streetNumber"));
        String streetName = request.getParameter("streetName");
        int postcode = Integer.parseInt(request.getParameter("postcode"));
        String suburb = request.getParameter("suburb");
        String city = request.getParameter("city");
        String state = request.getParameter("state");

        Address newAddress = new Address(name, streetNumber, streetName, postcode, suburb, city, state);
        try {
            addressDAO.insert(newAddress);
            response.sendRedirect("address?action=list");
        } catch (SQLException e) {
            throw new IOException("Error inserting address", e);
        }
    }

    // update Address method
    private void updateAddress(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        String name = request.getParameter("name");
        int streetNumber = Integer.parseInt(request.getParameter("streetNumber"));
        String streetName = request.getParameter("streetName");
        int postcode = Integer.parseInt(request.getParameter("postcode"));
        String suburb = request.getParameter("suburb");
        String city = request.getParameter("city");
        String state = request.getParameter("state");

        Address updatedAddress = new Address(id, name, streetNumber, streetName, postcode, suburb, city, state);
        try {
            addressDAO.update(updatedAddress);
            response.sendRedirect("address?action=list");
        } catch (SQLException e) {
            throw new IOException("Error updating address", e);
        }
    }

    // Delete Address method
    private void deleteAddress(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        try {
            addressDAO.deleteById(id);
            response.sendRedirect("address?action=list");
        } catch (SQLException e) {
            throw new IOException("Error deleting address", e);
        }
    }

    // List Address method
    private void listAddresses(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<Address> addresses = addressDAO.getAll();
            request.setAttribute("addressList", addresses);
            request.getRequestDispatcher("addressList.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Error retrieving addresses", e);
        }
    }
}

