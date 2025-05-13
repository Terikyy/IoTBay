package controllers;

import java.io.IOException;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import model.dao.OrderDAO;
import model.dao.UserDAO;
import model.Order;
import model.Address;

@WebServlet("/OrderController")
public class OrderController extends HttpServlet {
    // Controller for managing orders
    // This class will handle order-related operations such as creating, updating, and deleting orders and order items.

    private OrderDAO orderDAO;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        String name = request.getParameter("name");
        String email = request.getParameter("email");
        int stNum = Integer.parseInt(request.getParameter("stNum"));
        String stName = request.getParameter("stName");
        String state = request.getParameter("state");
        String suburb = request.getParameter("suburb");
        String city = request.getParameter("city");
        int zip = Integer.parseInt(request.getParameter("zip"));
        String phone = request.getParameter("phone");

        Address address = new Address(name, stNum, stName, zip, suburb, city, state);

        System.out.println("User: " + name + " (" + email + ") Address: " + stNum + " " + stName + " "
                            + state + " " + suburb + " " + zip + " " + city + " " + "Phone Number: " + phone);

        response.sendRedirect("payment.jsp");
    }
}