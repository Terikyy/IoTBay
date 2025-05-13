package controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/PayPalServlet")
public class PayPalServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String paymentMethod = request.getParameter("paymentMethod");
        HttpSession session = request.getSession();

        if (paymentMethod.equals("paypal")) {
            response.sendRedirect("https://www.paypal.com");
        } else {
            response.sendRedirect("payment.jsp");
        }
    }
}
