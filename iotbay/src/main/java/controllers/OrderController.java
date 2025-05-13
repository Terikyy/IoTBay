package controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.IDObject;
import model.Order;
import model.dao.OrderDAO;
import model.users.User;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

@WebServlet("/OrderController")
public class OrderController extends HttpServlet {
    // Controller for managing orders
    // This class will handle order-related operations such as creating, updating, and deleting orders and order items.

    private OrderDAO orderDAO;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        orderDAO = (OrderDAO) session.getAttribute("orderDAO");

        //TODO: Add OrderItem stuff

        User user = (User) session.getAttribute("user");
        Integer userId = user == null ? null : user.getUserID();

        Order order = new Order(userId, null, Order.ORDER_STATUS_PENDING, new Date(), 0.0); // TODO: Add total amount calculation
        try {
            IDObject.insert(orderDAO, order);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        session.setAttribute("orderId", order.getOrderID());

        response.sendRedirect("shipment.jsp");
    }

    public static Order deletOrder(int orderId, HttpSession session) throws SQLException {
        OrderDAO orderDAO = (OrderDAO) session.getAttribute("orderDAO");

        Order order = orderDAO.findById(orderId);
        if (order != null) {
            orderDAO.deleteById(orderId);
        }
        return order;
    }

    public static List<Order> getUserOrders(int userId, HttpSession session) throws SQLException {
        OrderDAO orderDAO = (OrderDAO) session.getAttribute("orderDAO");

        return orderDAO.findByUserId(userId);
    }
}