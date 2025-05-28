package controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.IDObject;
import model.Order;
import model.Payment;
import model.dao.OrderDAO;
import model.dao.PaymentDAO;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@WebServlet("/PaymentController")
public class PaymentController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processCreditCardPayment(request, response);
        } catch (SQLException | ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

        Double totalPrice = (Double) request.getAttribute("totalPrice");
        if (totalPrice == null) {
            totalPrice = 0.0;
            session.setAttribute("totalPrice", totalPrice);
        }

        request.setAttribute("totalPrice", totalPrice);
        request.getRequestDispatcher("payment.jsp").forward(request, response);

    }

    private void processCreditCardPayment(HttpServletRequest request, HttpServletResponse response)
            throws IOException, SQLException, ServletException, ParseException {
        HttpSession session = request.getSession();

        PaymentDAO paymentDAO = (PaymentDAO) session.getAttribute("paymentDAO");
        if (paymentDAO == null) {
            ConnServlet.updateDAOsPOST(request, response);
            return;
        }
        OrderDAO orderDAO = (OrderDAO) session.getAttribute("orderDAO");
        if (orderDAO == null) {
            ConnServlet.updateDAOsPOST(request, response);
            return;
        }

        String nameOnCard = request.getParameter("nameOnCard");
        String cardNumber = request.getParameter("cardNumber");
        String expiryDate = request.getParameter("expiryDate");
        if (new SimpleDateFormat("yyyy-MM-dd").parse(expiryDate).before(new Date())) {
            request.setAttribute("errorMessage", "Expiry date cannot be in the past.");
            request.getRequestDispatcher("payment.jsp").forward(request, response);
            return;
        }
        String cvv = request.getParameter("cvv");
        if (Integer.parseInt(cvv) > 999 || Integer.parseInt(cvv) < 100) {
            request.setAttribute("errorMessage", "CVV must be 3 digits.");
            request.getRequestDispatcher("payment.jsp").forward(request, response);
            return;
        }
        int orderId = (Integer) session.getAttribute("orderId");
        Double totalPrice = (Double) session.getAttribute("totalPrice");

        List<Payment> existing = paymentDAO.findByOrderId(orderId);
        if (!existing.isEmpty()) {
            // if a payment exists, reject creation
            request.setAttribute("message", "A payment already exists for this order.");
            request.getRequestDispatcher("payment.jsp").forward(request, response);
            return;
        }

        if (nameOnCard == null || cardNumber == null || expiryDate == null || cvv == null ||
                nameOnCard.isEmpty() || cardNumber.isEmpty() || expiryDate.isEmpty() || cvv.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid credit card details.");
            return;
        }
        System.out.println("Processing payment with card number: " + cardNumber);

        Payment payment = new Payment(orderId, "CreditCard", totalPrice, new java.util.Date(),
                Payment.PAYMENT_STATUS_PENDING);
        IDObject.insert(paymentDAO, payment);

        if ("Pay Now".equals(request.getParameter("action"))) {
            orderDAO.updateStatus(orderId, Order.ORDER_STATUS_PAID);
            payment.setPaymentStatus(Payment.PAYMENT_STATUS_COMPLETED);

            paymentDAO.update(payment);
        }

        session.setAttribute("paymentId", payment.getPaymentID());

        response.sendRedirect("shippingManagement.jsp");
    }

    public static Payment getPaymentById(int paymentId, HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        HttpSession session = request.getSession();

        PaymentDAO paymentDAO = (PaymentDAO) session.getAttribute("paymentDAO");

        if (paymentDAO == null) {
            ConnServlet.updateDAOsGET(request, response);
            return null;
        }

        return paymentDAO.findById(paymentId);
    }

}
