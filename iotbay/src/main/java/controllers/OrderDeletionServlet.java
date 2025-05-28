package controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.dao.OrderDAO;
import model.dao.OrderItemDAO;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/OrderDeletionServlet")
public class OrderDeletionServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String orderId = request.getParameter("orderId");
        String productId = request.getParameter("productId");

        HttpSession session = request.getSession();
        OrderItemDAO orderItemDAO = (OrderItemDAO) session.getAttribute("orderItemDAO");
        if (orderItemDAO == null) {
            ConnServlet.updateDAOsPOST(request, response);
            return;
        }

        String action = request.getParameter("action");
        if ("delete".equals(action)) {
            try {
                orderItemDAO.deleteByIds(Integer.parseInt(orderId), Integer.parseInt(productId));
                response.sendRedirect("update-order.jsp");
            } catch (SQLException e) {
                throw new ServletException("Error deleting Order Item", e);
            }
        }
    }
}
