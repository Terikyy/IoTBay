package controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.dao.PaymentDAO;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/PaymentDeletionServlet")
public class PaymentDeletionServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String sid = request.getParameter("paymentId");
        HttpSession session = request.getSession();
        PaymentDAO paymentDAO = (PaymentDAO) session.getAttribute("paymentDAO");
        if (paymentDAO == null) {
            // re-init DAOs if missing
            ConnServlet.updateDAOsGET(request, response);
            return;
        }
        try {
            paymentDAO.deleteById(Integer.parseInt(sid));
            // redirect back to the list
            response.sendRedirect(request.getContextPath() + "/PaymentListController");
        } catch (SQLException e) {
            throw new ServletException("Error deleting payment", e);
        }
    }
}