package controllers;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Order;
import model.ShippingManagement;
import model.dao.OrderDAO;
import model.dao.ShippingDAO;

@WebServlet("/ShippingController")
public class ShippingController extends HttpServlet {
    private ShippingDAO shippingDAO;
    private OrderDAO orderDAO;

    // doGet method
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws 
            ServletException, IOException {

        HttpSession session = request.getSession();

        shippingDAO = (ShippingDAO) session.getAttribute("shippingDAO");
        orderDAO = (OrderDAO) session.getAttribute("orderDAO");
        
        

        if (shippingDAO == null || orderDAO == null) {
            String currentURL = request.getRequestURI();
            if (request.getQueryString() != null) {
                currentURL += "?" + request.getQueryString();
            }
            response.sendRedirect(request.getContextPath() + "/Connservlet?redirectURL=" + currentURL);
            return;
        }

        
        String action = request.getParameter("action");
        // Create a new shipment
        if ("create".equals(action)) {
            // 1) read the orderId from the URL
            int orderId = Integer.parseInt(request.getParameter("orderId"));
            // 2) fetch the Order object
            Order order = null;
            try {
                order = orderDAO.findById(orderId);
                request.setAttribute("order", order);
                listShipments(request, response);
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                throw new ServletException("Error listing shipments", e);
            }
            // 3) stick it in request for the JSP
            request.setAttribute("order", order);

            // forward to JSP
            try {
                listShipments(request, response);
            } catch (SQLException e) {
                e.printStackTrace();
                throw new ServletException("Error listing shipments", e);
            }
            return;
        } 


        // Update an existing shipment
        if ("update".equals(action)) {
            // load existing Shipment for editing
            String shipmentIdParam = request.getParameter("shipmentId");
            if (shipmentIdParam == null || shipmentIdParam.isEmpty()) {
                throw new ServletException("Shipment ID is missing or invalid");
            }
            int shipmentId = Integer.parseInt(shipmentIdParam);
            // 1) read the orderId from the URL
            try {
                ShippingManagement updateShipment = shippingDAO.findById(shipmentId);
                request.setAttribute("updateShipment", updateShipment);
            } catch (SQLException e) {
                e.printStackTrace();
                throw new ServletException("Error finding shipment by ID", e);
            }
            // forward to JSP
            try {
                listShipments(request, response);
            } catch (SQLException e) {
                e.printStackTrace();
                throw new ServletException("Error listing shipments", e);
            }
            return;
        }

        else {
            try {
                listShipments(request, response);
            } catch (SQLException e) {
                e.printStackTrace();
                throw new ServletException("Error listing shipments", e);
            }
        }

    }






    // doPost method
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

            HttpSession session = request.getSession();

            shippingDAO = (ShippingDAO) session.getAttribute("shippingDAO");

            if (shippingDAO == null) {
                String currentURL = request.getRequestURI();
                if (request.getQueryString() != null) {
                    currentURL += "?" + request.getQueryString();
                }
                response.sendRedirect(request.getContextPath() + "/Connservlet?redirectURL=" + currentURL);
                return;
            }
            
                

            String action = request.getParameter("action");
    try {
        switch(action) {
        case "create":
            createShipment(request, response);
            return;

        case "update":
            updateShipment(request, response);
            return;

        case "delete":
            deleteShipment(request, response);
            return;

        default:
            listShipments(request, response);
            return;
        }
    } catch(SQLException e) {
        e.printStackTrace();
        throw new ServletException("Error handling shipment action", e);
    }


}

    //helper methods
    // List all shipments
    private void listShipments(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
        HttpSession session = request.getSession();
        Integer orderId = (Integer) session.getAttribute("orderId");
        List<ShippingManagement> shipments;
        if (orderId != null) {
          shipments = shippingDAO.findByOrderId(orderId);
        } else {
          shipments = List.of();
        }
        request.setAttribute("shipments", shipments);
        request.getRequestDispatcher("/shippingManagement.jsp").forward(request, response);
    }
    
    // Create a new shipment
    private void createShipment(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException, ServletException {

        String address        = request.getParameter("address");
        String shippingMethod = request.getParameter("shippingMethod");
        int    orderId        = Integer.parseInt(request.getParameter("orderId"));
        ShippingManagement shipment = new ShippingManagement(0, orderId, LocalDate.now(), address, shippingMethod, false);
        int newShipmentId = shippingDAO.insert(shipment);
        HttpSession session = request.getSession();
        session.setAttribute("lastShipmentId", newShipmentId);
        response.sendRedirect(request.getContextPath() + "/payment.jsp");
    }

    // Update an existing shipment
    private void updateShipment(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException, ServletException {
        int shipmentId = Integer.parseInt(request.getParameter("shipmentId"));
        int orderId = Integer.parseInt(request.getParameter("orderId"));
        String address = request.getParameter("address");
        String shippingMethod = request.getParameter("shippingMethod");

        ShippingManagement shipment = new ShippingManagement(shipmentId, orderId, LocalDate.now(), address, shippingMethod, false);
        shippingDAO.update(shipment);

        HttpSession session = request.getSession();
        session.setAttribute("orderId", orderId);
        response.sendRedirect(request.getContextPath() + "/payment.jsp");
    }

    // Delete a shipment
    private void deleteShipment(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException, ServletException {
        int shipmentId = Integer.parseInt(request.getParameter("shipmentId"));
        shippingDAO.deleteById(shipmentId);
        listShipments(request, response);
    }

}
