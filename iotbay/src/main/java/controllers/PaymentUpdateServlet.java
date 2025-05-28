package controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Payment;
import model.dao.PaymentDAO;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;


@WebServlet("/PaymentUpdateServlet")
public class PaymentUpdateServlet extends HttpServlet {
    private PaymentDAO paymentDAO;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // ensure DAO in session
        HttpSession session = req.getSession();
        paymentDAO = (PaymentDAO) session.getAttribute("paymentDAO");
        if (paymentDAO == null) {
            ConnServlet.updateDAOsGET(req, resp);
            return;
        }

        int pid = Integer.parseInt(req.getParameter("paymentId"));
        try {
            Payment p = paymentDAO.findById(pid);
            if (p == null || !Payment.PAYMENT_STATUS_PENDING.equals(p.getPaymentStatus())) {
                resp.sendRedirect("PaymentListController");
                return;
            }
            req.setAttribute("payment", p);
            req.getRequestDispatcher("/paymentUpdate.jsp").forward(req, resp);
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession();
        paymentDAO = (PaymentDAO) session.getAttribute("paymentDAO");
        if (paymentDAO == null) {
            ConnServlet.updateDAOsPOST(req, resp);
            return;
        }

        int pid = Integer.parseInt(req.getParameter("paymentId"));
        try {
            Payment p = paymentDAO.findById(pid);
            if (p != null && Payment.PAYMENT_STATUS_PENDING.equals(p.getPaymentStatus())) {
                p.setPaymentStatus(Payment.PAYMENT_STATUS_COMPLETED);
                p.setPaymentDate(new Date(System.currentTimeMillis()));
                paymentDAO.update(p);
            }
            resp.sendRedirect("PaymentListController");
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }
}