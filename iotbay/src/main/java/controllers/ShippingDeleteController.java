package controllers;

import java.io.IOException;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.dao.ShippingDAO;

@WebServlet("/ShippingDeleteController")
public class ShippingDeleteController extends HttpServlet {
    private ShippingDAO shippingDAO;

    @Override
protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    HttpSession session = request.getSession();
    shippingDAO = (ShippingDAO) session.getAttribute("shippingDAO");
    if (shippingDAO == null) {
        String currentURL = request.getRequestURI()
                          + (request.getQueryString()!=null?("?"+request.getQueryString()):"");
        response.sendRedirect(request.getContextPath() + "/Connservlet?redirectURL=" + currentURL);
        return;
    }

    String action = request.getParameter("action");
    if ("delete".equals(action)) {
        int id = Integer.parseInt(request.getParameter("shipmentId"));
        try {
            shippingDAO.deleteById(id);
        } catch (SQLException e) {
            throw new ServletException("Error deleting shipment", e);
        }
    }
    
    response.sendRedirect(request.getContextPath() + "/ShippingListController?showAll=1");
}
}