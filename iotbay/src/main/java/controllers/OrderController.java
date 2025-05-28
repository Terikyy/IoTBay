package controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.IDObject;
import model.Order;
import model.Product;
import model.dao.OrderDAO;
import model.dao.OrderItemDAO;
import model.lineproducts.OrderItem;
import model.users.User;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@WebServlet("/OrderController")
public class OrderController extends HttpServlet {
    // Controller for managing orders
    // This class will handle order-related operations such as creating, updating,
    // and deleting orders and order items.

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("=== doPost() triggered ===");
        HttpSession session = request.getSession();
        OrderDAO orderDAO = (OrderDAO) session.getAttribute("orderDAO");
        if (orderDAO == null) {
            System.out.println("OrderDAO is null, updating DAOs...");
            ConnServlet.updateDAOsPOST(request, response);
            return;
        }

        OrderItemDAO orderItemDAO = (OrderItemDAO) session.getAttribute("orderItemDAO");
        if (orderItemDAO == null) {
            ConnServlet.updateDAOsPOST(request, response);
            return;
        }

        User user = (User) session.getAttribute("user");
        Integer userId = user == null ? null : user.getUserID();
        double totalPrice;
        try {
            totalPrice = Double.parseDouble(request.getParameter("totalPrice"));
            System.out.println("TotalPrice parameter: " + totalPrice);
        } catch (NumberFormatException e) {
            totalPrice = 0.0;
        }
        System.out.println("Received total price: " + totalPrice);

        Order order = new Order(userId, Order.ORDER_STATUS_PENDING, new Date(System.currentTimeMillis()), totalPrice); // TODO: Add total amount
        // calculation
        try {
            IDObject.insert(orderDAO, order);
        } catch (SQLException e) {
            System.out.println("Error inserting order: " + e.getMessage());
            throw new RuntimeException(e);
        }
        session.setAttribute("orderId", order.getOrderID());
        session.setAttribute("totalPrice", order.getTotalPrice());

        List<Map<String, Object>> cartItems = (List<Map<String, Object>>) session.getAttribute("cartItems");

        if (cartItems != null) {
            for (Map<String, Object> cartItem : cartItems) {
                Product product = (Product) cartItem.get("product");
                int productId = product.getProductID();
                int quantity = (int) cartItem.get("quantity");
                double price = product.getPrice();

                OrderItem orderItem = new OrderItem(productId, order.getOrderID(), quantity,
                        price);
                try {
                    orderItemDAO.insert(orderItem);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        session.setAttribute("orderCreated", true);

        session.removeAttribute("cartItems");
        session.removeAttribute("cartTotal");
        session.removeAttribute("cart");

        System.out.println("Redirecting to payment.jsp...");
        response.sendRedirect("payment.jsp");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();

        OrderDAO orderDAO = (OrderDAO) session.getAttribute("orderDAO");
        if (orderDAO == null) {
            ConnServlet.updateDAOsGET(request, response);
            return;
        }

        String action = request.getParameter("action");
        if ("delete".equals(action)) {
            int orderId = Integer.parseInt(request.getParameter("orderId"));
            try {
                deleteOrder(orderId, session);
                System.out.println("Order deleted: " + orderId);
                response.sendRedirect("order.jsp");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static Order deleteOrder(int orderId, HttpSession session) throws SQLException {
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

    public static List<Order> queryUserOrders(int userId, String query, HttpSession session) throws SQLException {
        List<Order> orders = new ArrayList<>();
        for (Order order : getUserOrders(userId, session)) {
            if (String.valueOf(order.getOrderID()).contains(query) ||
                    order.getOrderDate().toString().contains(query)) {
                orders.add(order);
            }
        }
        return orders;
    }

    public static Order getOrderById(int orderId, HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        HttpSession session = request.getSession();

        OrderDAO orderDAO = (OrderDAO) session.getAttribute("orderDAO");

        if (orderDAO == null) {
            ConnServlet.updateDAOsGET(request, response);
            return null;
        }

        return orderDAO.findById(orderId);
    }

}
