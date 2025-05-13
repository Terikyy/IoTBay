package controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Address;
import model.ShippingManagement;
import model.dao.ShipmentDAO;

@WebServlet("/ShipmentController")
public class ShipmentController extends HttpServlet {
    private ShipmentDAO shipmentDAO;

    @Override
    public void init() throws ServletException {
        try {
            // Use the DB class URL instead of a hardcoded path
            String dbURL = "jdbc:sqlite:" + getServletContext().getRealPath("/WEB-INF/database/iotbay.db");
            
            // Load the JDBC driver first
            Class.forName("org.sqlite.JDBC");
            
            Connection connection = DriverManager.getConnection(dbURL);
            shipmentDAO = new ShipmentDAO(connection);
            
            // Log successful initialization
            System.out.println("ShipmentDAO initialized with connection to: " + dbURL);
        } catch (SQLException e) {
            throw new ServletException("Failed to initialize ShipmentDAO: " + e.getMessage(), e);
        } catch (ClassNotFoundException e) {
            throw new ServletException("JDBC driver not found: " + e.getMessage(), e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) action = "list";

        try {
            switch (action) {
                case "new":
                    showNewForm(request, response);
                    break;
                case "create":
                case "insert":
                    insertShipment(request, response);
                    break;
                case "delete":
                    deleteShipment(request, response);
                    break;
                case "edit":
                    showEditForm(request, response);
                    break;
                case "update":
                    updateShipment(request, response);
                    break;
                default:
                    listShipments(request, response);
                    break;
            }
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        int stNum = Integer.parseInt(request.getParameter("stNum"));
        String stName = request.getParameter("stName");
        String state = request.getParameter("state");
        String suburb = request.getParameter("suburb");
        String city = request.getParameter("city");
        int zip = Integer.parseInt(request.getParameter("zip"));
        String phone = request.getParameter("phone");

        response.sendRedirect("payment.jsp");
    }

    private void listShipments(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<ShippingManagement> shipments = shipmentDAO.getAll();
            request.setAttribute("shipments", shipments);
            // clear any editing state
            request.setAttribute("editingShipment", null);
            request.getRequestDispatcher("/shippingManagement.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Error retrieving shipments", e);
        }
    }

    private void insertShipment(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
        int orderId = Integer.parseInt(request.getParameter("orderId"));
        LocalDate date = LocalDate.parse(request.getParameter("shipmentDate"));
        String address = request.getParameter("address");
        String deliveryMethod = request.getParameter("deliveryMethod");
        ShippingManagement shipment = new ShippingManagement(0, orderId, date, address, deliveryMethod);
        shipmentDAO.insert(shipment);
        response.sendRedirect("shipment?action=list");
    }

    private void updateShipment(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
        int id = Integer.parseInt(request.getParameter("shipmentId"));
        ShippingManagement existing = shipmentDAO.findById(id);
        if (existing == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Shipment not found");
            return;
        }
        int orderId = Integer.parseInt(request.getParameter("orderId"));
        LocalDate date = LocalDate.parse(request.getParameter("shipmentDate"));
        String address = request.getParameter("address");
        String deliveryMethod = request.getParameter("deliveryMethod");
        // replace with updated object
        ShippingManagement updated = new ShippingManagement(id, orderId, date, address, deliveryMethod);
        shipmentDAO.update(updated);
        response.sendRedirect("shipment?action=list");
    }

    private void deleteShipment(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
        int id = Integer.parseInt(request.getParameter("shipmentId"));
        ShippingManagement existing = shipmentDAO.findById(id);
        if (existing == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Shipment not found");
            return;
        }
        shipmentDAO.deleteById(id);
        response.sendRedirect("shipment?action=list");
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
        // reuse list method to load shipments and show blank form
        listShipments(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
        int id = Integer.parseInt(request.getParameter("shipmentId"));
        ShippingManagement existing = shipmentDAO.findById(id);
        request.setAttribute("editingShipment", existing);
        listShipments(request, response);
    }
}
