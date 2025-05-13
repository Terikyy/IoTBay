package controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Order;
import model.dao.OrderDAO;
import model.users.User;

import java.io.IOException;

@WebServlet("/OrderController")
public class OrderController extends HttpServlet {
    // Controller for managing orders
    // This class will handle order-related operations such as creating, updating, and deleting orders and order items.

    private OrderDAO orderDAO;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        orderDAO = (OrderDAO) session.getAttribute("orderDAO");

        String name = request.getParameter("name");
        String email = request.getParameter("email");

        User user = (User) session.getAttribute("user");

        Order order = new Order();

        response.sendRedirect("shipment.jsp");
    }
}