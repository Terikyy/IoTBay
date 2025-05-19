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

@WebServlet("/PaymentController")
public class PaymentController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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

    private void processCreditCardPayment(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
        HttpSession session = request.getSession();

        System.out.println("Test1");
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
        //int orderId = 1;//get from shipping 

        if (nameOnCard == null || cardNumber == null || expiryDate == null || cvv == null ||
                nameOnCard.isEmpty() || cardNumber.isEmpty() || expiryDate.isEmpty() || cvv.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid credit card details.");
            return;
        }
        System.out.println("Processing payment with card number: " + cardNumber);

        // payment object is filelr for now hardcoded values, need to pass in values from cart when ready
        Payment payment = new Payment(orderId, "CreditCard", 100.0, new java.util.Date(), "Pending");
        IDObject.insert(paymentDAO, payment);

        orderDAO.updateStatus(orderId, Order.ORDER_STATUS_PAID);
        //redirect to order for now, decide if we want a order confirmation page. what if payment fails? stay here or return to order form?
        response.sendRedirect("confirmation.jsp");
    }
}

