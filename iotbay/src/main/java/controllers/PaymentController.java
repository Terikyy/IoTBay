package controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServlet;

import java.io.IOException;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Payment;
import model.dao.PaymentDAO;

@WebServlet("/PaymentController")
public class PaymentController extends HttpServlet {

    @Override 
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession();
        
        PaymentDAO paymentDAO = (PaymentDAO) session.getAttribute("paymentDAO");
        if (paymentDAO == null) {
            throw new ServletException("PaymentDAO not found in session");
        }

        String paymentMethod = request.getParameter(("paymentMethod"));

        try {
            if ("PayPal".equals(paymentMethod)){
                response.sendRedirect("https://www.paypal.com");
                return;
            } else if ("CreditCard".equals(paymentMethod)){
                processCreditCardPayment(request, response, paymentDAO);
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid payment method");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred while processing the payment");
        }
    }

private void processCreditCardPayment(HttpServletRequest request, HttpServletResponse response, PaymentDAO paymentDAO) throws IOException, SQLException {

    String cardNumber = request.getParameter("cardNumber");
    String expiryDate = request.getParameter("expiryDate");
    String cvv = request.getParameter("cvv");

        if (cardNumber == null || expiryDate == null || cvv == null ||
            cardNumber.isEmpty() || expiryDate.isEmpty() || cvv.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid credit card details.");
            return;
        }
        System.out.println("Processing payment with card number: " + cardNumber);

        // payment object is filelr for now hardcoded values, need to pass in values from cart when ready
        Payment payment = new Payment(0, 1, "CreditCard", 100.0, new java.util.Date(), "Pending");
        paymentDAO.insert(payment);

        //redirect to order for now, decide if we want a order confirmation page. what if payment fails? stay here or return to order form?
        response.sendRedirect("order.jsp");

    }
}

