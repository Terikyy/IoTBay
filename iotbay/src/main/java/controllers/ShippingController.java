package controllers;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
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
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            String action = request.getParameter("action");
            // ...but allow them to view the create‐form:
            if ("create".equals(action)) {
                // forward to JSP so they can fill the create form
                request.getRequestDispatcher("/shippingManagement.jsp")
                       .forward(request, response);
                return;
            }
            // block everything else:
            response.sendRedirect(request.getContextPath() + "/LoginController");
            return;
        }

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

        
        if ("search".equals(action)) {
            String sidParam  = request.getParameter("shipmentId");
            String dateParam = request.getParameter("shipmentDate");
    
            List<ShippingManagement> results = new ArrayList<>();
    
            // 1) If they gave an ID, fetch that one and verify it belongs to the user
            if (sidParam != null && !sidParam.isBlank()) {
                int sid = Integer.parseInt(sidParam);
                ShippingManagement s = null;
                try {
                    s = shippingDAO.findById(sid);
                } catch (SQLException e) {
                    e.printStackTrace();
                    throw new ServletException("Error finding shipment by ID", e);
                }
                if (s != null) {
                    Order owner = null;
                    try {
                        owner = orderDAO.findById(s.getOrderId());
                    } catch (SQLException e) {
                        e.printStackTrace();
                        throw new ServletException("Error finding order by ID", e);
                    }
                    if (owner != null && owner.getUserID().equals(userId)) {
                        // If date also provided, double-check it matches
                        if (dateParam == null
                          || dateParam.isBlank()
                          || s.getShipmentDate().toString().equals(dateParam)) {
                            results.add(s);
                        }
                    }
                }
            }
            // 2) Else if they gave a date only, fetch all for that date
            else if (dateParam != null && !dateParam.isBlank()) {
                LocalDate d = LocalDate.parse(dateParam);
                try {
                    results = shippingDAO.findByUserIdAndDate(userId, d);
                } catch (SQLException e) {
                    e.printStackTrace();
                    throw new ServletException("Error finding shipments by user ID and date", e);
                }
            }
            // 3) Otherwise leave results empty (or you could default to list all)
    
            // 4) Forward filtered list to JSP
            request.setAttribute("shipments", results);
            request.getRequestDispatcher("/shippingManagement.jsp")
                   .forward(request, response);
            return;
        }




        
        if ("create".equals(action)) {
            // 1) read the orderId from the URL
            int orderId = Integer.parseInt(request.getParameter("orderId"));
            // 2) fetch the Order object
            Order order = null;
            try {
                order = orderDAO.findById(orderId);
                request.setAttribute("order", order);
                listShipments(request, response, userId);
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                throw new ServletException("Error listing shipments", e);
            }
            // 3) stick it in request for the JSP
            request.setAttribute("order", order);

            // forward to your JSP
            try {
                listShipments(request, response, userId);
            } catch (SQLException e) {
                e.printStackTrace();
                throw new ServletException("Error listing shipments", e);
            }
            return;
        } 

        if ("update".equals(action)) {
            // load existing Shipment for editing
            int shipmentId = Integer.parseInt(request.getParameter("shipmentId"));
            // 1) read the orderId from the URL
            try {
                ShippingManagement updateShipment = shippingDAO.findById(shipmentId);
                request.setAttribute("updateShipment", updateShipment);
            } catch (SQLException e) {
                e.printStackTrace();
                throw new ServletException("Error finding shipment by ID", e);
            }
            

            // forward to your JSP
            try {
                listShipments(request, response, userId);
            } catch (SQLException e) {
                e.printStackTrace();
                throw new ServletException("Error listing shipments", e);
            }
            return;
        }



        else {
            try {
                listShipments(request, response, (Integer) session.getAttribute("userId"));
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
            Integer userId = (Integer) session.getAttribute("userId");

                // If not logged in, block all POSTs:

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
            listShipments(request, response, (Integer) session.getAttribute("userId"));
            return;
        }
    } catch(SQLException e) {
        e.printStackTrace();
        throw new ServletException("Error handling shipment action", e);
    }


}

    //helper methods
    // List all shipments
    private void listShipments(HttpServletRequest request, HttpServletResponse response, int userId) throws SQLException, ServletException, IOException {
        List<ShippingManagement> shipments = shippingDAO.findByUserId(userId);
        request.setAttribute("shipments", shipments);
        request.getRequestDispatcher("/shippingManagement.jsp").forward(request, response);
    }
    
    // Create a new shipment
    private void createShipment(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException, ServletException {

        String address = request.getParameter("address");
        String shippingMethod = request.getParameter("shippingMethod");
        int orderId = Integer.parseInt(request.getParameter("orderId"));
        ShippingManagement shipment = new ShippingManagement(0, orderId, LocalDate.now(), address, shippingMethod, false);
        shippingDAO.insert(shipment);

        int userId = (Integer) request.getSession().getAttribute("userId");
        listShipments(request, response, userId);
    }

    // Update an existing shipment
    private void updateShipment(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException, ServletException {
        int shipmentId = Integer.parseInt(request.getParameter("shipmentId"));
        int orderId = Integer.parseInt(request.getParameter("orderId"));
        String address = request.getParameter("address");
        String shippingMethod = request.getParameter("shippingMethod");

        ShippingManagement shipment = new ShippingManagement(shipmentId, orderId, LocalDate.now(), address, shippingMethod, false);
        shippingDAO.update(shipment);

        int userId = (Integer) request.getSession().getAttribute("userId");
        listShipments(request, response, userId);
    }

    // Delete a shipment
    private void deleteShipment(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException, ServletException {
        int shipmentId = Integer.parseInt(request.getParameter("shipmentId"));
        shippingDAO.deleteById(shipmentId);
        int userId = (Integer) request.getSession().getAttribute("userId");
        listShipments(request, response, userId);
    }

}
