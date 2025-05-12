package controllers;

import java.io.IOException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import model.dao.OrderDAO;

@WebServlet("/OrderController")
public class OrderController extends HttpServlet {
    // Controller for managing orders
    // This class will handle order-related operations such as creating, updating, and deleting orders and order items.

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        String fName = request.getParameter("fName");
        String lName = request.getParameter("lName");
        String email = request.getParameter("email");
        String address = request.getParameter("address");
        String country = request.getParameter("country");
        String state = request.getParameter("state");
        String subCit = request.getParameter("subCit");
        String zip = request.getParameter("zip");
        String phone = request.getParameter("phone");

        System.out.println("User: " + fName + " " + lName + " (" + email + ") Address:" + address + " " + country + " "
                            + state + " " + subCit + " " + zip + " " + "Phone Number: " + phone);

        response.sendRedirect("payment.jsp");
    }
}
