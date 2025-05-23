package controllers;

import java.io.IOException;
import java.sql.SQLException;

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

@WebServlet("/PaymentController")
public class PaymentController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processCreditCardPayment(request, response);
        } catch (SQLException e) {
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
            throws IOException, SQLException {
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
        String cvv = request.getParameter("cvv");
        int orderId = (Integer) session.getAttribute("orderId");
        Double totalPrice = (Double) session.getAttribute("totalPrice");

        if (nameOnCard == null || cardNumber == null || expiryDate == null || cvv == null ||
                nameOnCard.isEmpty() || cardNumber.isEmpty() || expiryDate.isEmpty() || cvv.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid credit card details.");
            return;
        }
        System.out.println("Processing payment with card number: " + cardNumber);

        Payment payment = new Payment(orderId, "CreditCard", totalPrice, new java.util.Date(), Payment.PAYMENT_STATUS_PENDING);
        IDObject.insert(paymentDAO, payment);


        if ("Pay Now".equals(request.getParameter("action"))) {
            orderDAO.updateStatus(orderId, Order.ORDER_STATUS_PAID);
            payment.setPaymentStatus(Payment.PAYMENT_STATUS_COMPLETED);
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
