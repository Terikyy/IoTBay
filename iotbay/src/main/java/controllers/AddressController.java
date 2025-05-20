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
import model.Address;
import model.dao.AddressDAO;
import model.users.User;

@WebServlet("/address")
public class AddressController extends HttpServlet {

  

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
    
        if ("list".equals(action)) {
            listAddresses(request, response);
        } else if ("edit".equals(action)) {
            editAddress(request, response); // Add this line
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("insert".equals(action)) {
            insertAddress(request, response);
        } else if ("update".equals(action)) {
            updateAddress(request, response); // This handles the update
        } else if ("delete".equals(action)) {
            deleteAddress(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action");
        }
    }
    
    //edit address method
    private void editAddress(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    HttpSession session = request.getSession();
    AddressDAO addressDAO = (AddressDAO) session.getAttribute("addressDAO");
    if (addressDAO == null) {
        throw new IOException("AddressDAO is not initialized in the session.");
    }

    // Retrieve the user from the session
    User user = (User) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    try {
        int userId = user.getId(); // Assuming User object has a getId() method
        int id = Integer.parseInt(request.getParameter("id"));
        Address address = addressDAO.findByIdAndUserId(id, userId); // Fetch address by ID and userId
        if (address == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Address not found or access denied");
            return;
        }
        request.setAttribute("address", address);
        request.getRequestDispatcher("addressForm.jsp").forward(request, response);
    } catch (SQLException e) {
        throw new ServletException("Error retrieving address for editing", e);
    } catch (NumberFormatException e) {
        throw new IOException("Invalid address ID for editing", e);
    }
}
    
    // Insert Address method
    private void insertAddress(HttpServletRequest request, HttpServletResponse response)
        throws IOException {
        HttpSession session = request.getSession();
        AddressDAO addressDAO = (AddressDAO) session.getAttribute("addressDAO");
        if (addressDAO == null) {
            throw new IOException("AddressDAO is not initialized in the session.");
        }

        try {
            String name = request.getParameter("name");
            int streetNumber = Integer.parseInt(request.getParameter("streetNumber"));
            String streetName = request.getParameter("streetName");
            int postcode = Integer.parseInt(request.getParameter("postcode"));
            String suburb = request.getParameter("suburb");
            String city = request.getParameter("city");
            String state = request.getParameter("state");

            Address newAddress = new Address(name, streetNumber, streetName, postcode, suburb, city, state);
            addressDAO.insert(newAddress);
            response.sendRedirect("address?action=list");
        } catch (SQLException e) {
            throw new IOException("Error inserting address", e);
        }
    }

    // update Address method
    private void updateAddress(HttpServletRequest request, HttpServletResponse response)
        throws IOException {
    HttpSession session = request.getSession();
    AddressDAO addressDAO = (AddressDAO) session.getAttribute("addressDAO");
    if (addressDAO == null) {
        throw new IOException("AddressDAO is not initialized in the session.");
    }

    // Retrieve the user from the session
    User user = (User) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    try {
        int userId = user.getId(); // Assuming User object has a getId() method
        int id = Integer.parseInt(request.getParameter("id"));
        Address address = addressDAO.findByIdAndUserId(id, userId); // Validate ownership
        if (address == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Address not found or access denied");
            return;
        }

        String name = request.getParameter("name");
        int streetNumber = Integer.parseInt(request.getParameter("streetNumber"));
        String streetName = request.getParameter("streetName");
        int postcode = Integer.parseInt(request.getParameter("postcode"));
        String suburb = request.getParameter("suburb");
        String city = request.getParameter("city");
        String state = request.getParameter("state");

        Address updatedAddress = new Address(id, name, streetNumber, streetName, postcode, suburb, city, state);
        addressDAO.update(updatedAddress);
        response.sendRedirect("address?action=list");
    } catch (SQLException e) {
        throw new IOException("Error updating address", e);
    } catch (NumberFormatException e) {
        throw new IOException("Invalid input for address update", e);
    }
}

    // delete Address method
    private void deleteAddress(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession();
        AddressDAO addressDAO = (AddressDAO) session.getAttribute("addressDAO");
        if (addressDAO == null) {
            throw new IOException("AddressDAO is not initialized in the session.");
        }

        // Retrieve the user from the session
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        try {
            int userId = user.getId(); // Assuming User object has a getId() method
            int id = Integer.parseInt(request.getParameter("id"));
            Address address = addressDAO.findByIdAndUserId(id, userId); // Validate ownership
            if (address == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Address not found or access denied");
                return;
            }
            addressDAO.deleteById(id);
            response.sendRedirect("address?action=list");
        } catch (SQLException e) {
            throw new IOException("Error deleting address", e);
        } catch (NumberFormatException e) {
            throw new IOException("Invalid address ID for deletion", e);
        }
}

    // List Address method
    private void listAddresses(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        AddressDAO addressDAO = (AddressDAO) session.getAttribute("addressDAO");
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        try {
            int userId = user.getId(); // get the user's ID
            List<Address> addresses = addressDAO.getByUserId(userId); // fetch by userId
            request.setAttribute("addressList", addresses);
            request.getRequestDispatcher("addressList.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Error retrieving addresses", e);
        }
        }
}



