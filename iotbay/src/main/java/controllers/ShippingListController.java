package controllers;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.ShippingManagement;
import model.dao.ShippingDAO;
import model.dao.UserDAO;



@WebServlet("/ShippingListController")
public class ShippingListController extends HttpServlet {
    private ShippingDAO shippingDAO;
    private UserDAO userDAO;

    
 

    // doGet Method
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
            HttpSession session = request.getSession();
            shippingDAO = (ShippingDAO) session.getAttribute("shippingDAO");
            userDAO = (UserDAO) session.getAttribute("userDAO");
            
        
        try {
            if (shippingDAO == null) {
                String currentURL = request.getRequestURI();
                if (request.getQueryString() != null) {
                    currentURL += "?" + request.getQueryString();
                }
                response.sendRedirect(request.getContextPath() + "/Connservlet?redirectURL=" + currentURL);
                return;
        }
        
        // Check if the user is logged in
        // Showing shipments created by customer user
        Integer userID = (Integer)session.getAttribute("userID");
        List<ShippingManagement> all;
        if (userID != null) { // logged in user
            all = shippingDAO.findByUserId(userID);
        } else { // guest user
            Integer guestId = (Integer)session.getAttribute("guestShipping");
            if (guestId != null) {
              ShippingManagement one = shippingDAO.findById(guestId);
              all = (one == null) ? Collections.emptyList() : Collections.singletonList(one);
            } else {
              all = Collections.emptyList();
            }
        }

        // otherwise filter by shipmentId and/or shipmentDate
        String sid  = request.getParameter("shipmentId");
        String date = request.getParameter("shipmentDate");

        boolean noFilters = (sid == null || sid.isEmpty())
                 && (date == null || date.isEmpty());
        if (request.getParameter("showAll") != null || noFilters) {
            request.setAttribute("shipments", all);
            request.getRequestDispatcher("/shippingList.jsp")
                .forward(request, response);
            return;
        }

        List<ShippingManagement> filtered = all;
        // Filter by shipmentId and/or shipmentDate
        if (!sid.isEmpty() && !date.isEmpty()) {
        int id = Integer.parseInt(sid);
        LocalDate d = LocalDate.parse(date);
        filtered = all.stream()

            .filter(s -> s.getShipmentId() == id
                       && s.getShipmentDate().equals(d))
            .collect(Collectors.toList());
        } else if (!sid.isEmpty()) { //filter by shipmentId only
        int id = Integer.parseInt(sid);
        filtered = filtered.stream()
            .filter(s -> s.getShipmentId() == id)
            .collect(Collectors.toList());
        }else if (date!=null && !date.isEmpty()) { //filter by shipmentDate only
            LocalDate d = LocalDate.parse(date);
            filtered = filtered.stream()
            .filter(s -> s.getShipmentDate().equals(d))
            .collect(Collectors.toList());
        }
        
        request.setAttribute("shipments", filtered);

        request.getRequestDispatcher("/shippingList.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Error handling shipment action", e);
        }
    }
}

