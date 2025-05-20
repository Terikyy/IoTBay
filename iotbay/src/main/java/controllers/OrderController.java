package controllers;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

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

@WebServlet("/OrderController")
public class OrderController extends HttpServlet {
    // Controller for managing orders
    // This class will handle order-related operations such as creating, updating,
    // and deleting orders and order items.

    private OrderDAO orderDAO;
    // private OrderItemDAO orderItemDAO;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("=== doPost() triggered ===");
        HttpSession session = request.getSession();
        orderDAO = (OrderDAO) session.getAttribute("orderDAO");
        if (orderDAO == null) {
            System.out.println("OrderDAO is null, updating DAOs...");
            ConnServlet.updateDAOsGET(request, response);
            return;
        }

        // orderItemDAO = (OrderItemDAO) session.getAttribute("orderitemDAO");
        // if (orderItemDAO == null) {
        // ConnServlet.updateDAOsGET(request, response);
        // return;
        // }

        User user = (User) session.getAttribute("user");
        Integer userId = user == null ? null : user.getUserID();
        session.setAttribute("userID", userId);
        double totalPrice = 0.0;
        try {
            totalPrice = Double.parseDouble(request.getParameter("totalPrice"));
            System.out.println("TotalPrice parameter: " + totalPrice);
        } catch (NumberFormatException e) {
            totalPrice = 0.0;
        }
        System.out.println("Received total price: " + totalPrice);

        Order order = new Order(userId, Order.ORDER_STATUS_PENDING, new Date(), totalPrice); // TODO: Add total amount
                                                                                             // calculation
        try {
            IDObject.insert(orderDAO, order);
        } catch (SQLException e) {
            System.out.println("Error inserting order: " + e.getMessage());
            throw new RuntimeException(e);
        }
        session.setAttribute("orderId", order.getOrderID());
        session.setAttribute("totalPrice", order.getTotalPrice());

        // <-- Add this line
        // List<Map<String, Object>> cartItems = (List<Map<String, Object>>)
        // session.getAttribute("cartItems");

        // if (cartItems != null) {
        // for (Map<String, Object> cartItem : cartItems) {
        // Product product = (Product) cartItem.get("product");
        // int productId = product.getProductID();
        // int quantity = (int) cartItem.get("quantity");
        // double price = product.getPrice();

        // OrderItem orderItem = new OrderItem(order.getOrderID(), productId, quantity,
        // price);
        // try {
        // orderItemDAO.insert(orderItem);
        // } catch (SQLException e) {
        // throw new RuntimeException(e);
        // }
        // }
        // }

        // session.setAttribute("orderCreated", true);

        // session.removeAttribute("cartItems");
        // session.removeAttribute("cartTotal");
        // session.removeAttribute("cart");

        System.out.println("Redirecting to shippingManagement.jsp...");
        response.sendRedirect("shippingManagement.jsp");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Boolean orderCreated = (Boolean) session.getAttribute("orderCreated");

        if (orderCreated != null && orderCreated) {
            session.removeAttribute("orderCreated");
            response.sendRedirect("shippingManagement.jsp");
            return;
        } else {
            orderDAO = (OrderDAO) session.getAttribute("orderDAO");
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
