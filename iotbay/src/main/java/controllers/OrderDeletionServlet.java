package controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.dao.OrderDAO;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/OrderDeletionServlet")
public class OrderDeletionServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String orderId = request.getParameter("orderId");
        HttpSession session = request.getSession();
        OrderDAO orderDAO = (OrderDAO) session.getAttribute("orderDAO");
        if (orderDAO == null) {
            ConnServlet.updateDAOsPOST(request, response);
            return;
        }

        try {
            orderDAO.deleteById(Integer.parseInt(orderId));
            response.sendRedirect("order.jsp");
        } catch (SQLException e) {
            throw new ServletException("Error deleting Order", e);
        }
    }
}
