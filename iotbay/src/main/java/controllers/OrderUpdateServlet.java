package controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Order;
import model.Product;
import model.dao.ProductDAO;
import model.dao.OrderDAO;
import model.dao.OrderItemDAO;
import model.lineproducts.OrderItem;
import model.users.User;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/OrderUpdateServlet")
public class OrderUpdateServlet extends HttpServlet {
    // Controller for managing orders
    // This class will handle order-related operations such as creating, updating, and deleting orders and order items.

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        OrderDAO orderDAO = (OrderDAO) session.getAttribute("orderDAO");
        if (orderDAO == null) {
            ConnServlet.updateDAOsPOST(request, response);
            return;
        }


        User user = (User) session.getAttribute("user");
        Integer userId = user == null ? null : user.getUserID();

        response.sendRedirect("update-order.jsp");
    }

    public static Order deleteOrder(int orderId, HttpSession session) throws SQLException {
        OrderDAO orderDAO = (OrderDAO) session.getAttribute("orderDAO");

        Order order = orderDAO.findById(orderId);
        if (order != null) {
            orderDAO.deleteById(orderId);
        }
        return order;
    }

    public static List<OrderItem> getOrderItems(int orderID, HttpSession session) throws SQLException {
        OrderItemDAO orderItemDAO = (OrderItemDAO) session.getAttribute("orderItemDAO");

        return orderItemDAO.findByOrderId(orderID);
    }

    public static Product getProductById(int productId, HttpSession session) throws SQLException {
        ProductDAO productDAO = (ProductDAO) session.getAttribute("productDAO");

        return productDAO.findById(productId);
    }
}