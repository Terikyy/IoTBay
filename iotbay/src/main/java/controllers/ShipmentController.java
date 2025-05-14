package controllers;


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.ShippingManagement;
import model.dao.ShipmentDAO;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;


@WebServlet("/ShipmentController")
public class ShipmentController extends HttpServlet {
    private ShipmentDAO shipmentDAO;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws
            ServletException, IOException {
        HttpSession session = request.getSession();
        shipmentDAO = (ShipmentDAO) session.getAttribute("shipmentDAO");
        if (shipmentDAO == null) {
            ConnServlet.updateDAOsGET(request, response);
            return;
        }


        String ShippingId = request.getParameter("ShippingId");
        String orderId = request.getParameter("orderId");
        String shipmentDate = request.getParameter("shipmentDate");
        String address = request.getParameter("address");
        String deliveryMethod = request.getParameter("deliveryMethod");

        System.out.println("Shipping ID: " + ShippingId);
        System.out.println("Order ID: " + orderId);
        System.out.println("Shipment Date: " + shipmentDate);
        System.out.println("Address: " + address);
        System.out.println("Delivery Method: " + deliveryMethod);
        // Assuming you have a method to create a ShippingManagement object
        // ShippingManagement shipment = new ShippingManagement(Integer.parseInt(ShippingId), Integer.parseInt(orderId), LocalDate.parse(shipmentDate), address, deliveryMethod);
        // shipmentDAO.insert(shipment);
        // Redirect to a confirmation page or display a success message
        response.sendRedirect("index.jsp");


    }

    public static List<ShippingManagement> getUserOrders(int userId, int orderId, HttpSession session) throws SQLException {
        ShipmentDAO shipmentDAO = (ShipmentDAO) session.getAttribute("ShipmentDAO");

        ShippingManagement shipment = shipmentDAO.findById(orderId);
        return List.of(shipment);
    }
}
