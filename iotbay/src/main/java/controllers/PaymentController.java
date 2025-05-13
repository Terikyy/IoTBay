package controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import model.Payment;
import model.dao.PaymentDAO;

public class PaymentController {
    // This class handles payment processing and related operations
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        String paymentMethod = request.getParameter("paymentMethod");

        if ("PayPal".equals(paymentMethod)) {
            response.sendRedirect("https://www.paypal.com");
        } else if ("CreditCard".equals(paymentMethod)) {
            processCreditCardPayment(request);


        // Handle payment processing here
        String cardNumber = request.getParameter("cardNumber");
        String cardExpiry = request.getParameter("cardExpiry");
        String cvc = request.getParameter("cvc");

        if (cardNumber == null || expiryDate == null || cvc == null ||
            cardNumber.isEmpty() || expiryDate.isEmpty() || cvc.isEmpty()) {

                request.setAttribute("error", "Invalid credntials. Please try again.");
                request.getRequestDispatcher("payment.jsp").forward(request, response);

        } 

        }
        // Validate card details
        // Process payment logic here
        System.out.println("Processing payment with card number: " + cardNumber);
    }
}
