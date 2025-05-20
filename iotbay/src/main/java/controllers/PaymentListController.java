package controllers;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.Payment;
import model.dao.PaymentDAO;

@WebServlet("/PaymentListController")
public class PaymentListController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        PaymentDAO paymentDAO = (PaymentDAO) session.getAttribute("paymentDAO");
        if (paymentDAO == null) {
            // re-init DAOs if needed
            response.sendRedirect(request.getContextPath()
                    + "/ConnServlet?redirectURL=PaymentListController");
            return;
        }

        List<Payment> all;
        try {
            all = paymentDAO.getAll();
        } catch (SQLException e) {
            throw new ServletException(e);
        }

        String pid = request.getParameter("paymentId");
        String date = request.getParameter("paymentDate");
        boolean noFilters = (pid == null || pid.isEmpty())
                && (date == null || date.isEmpty());

        if (request.getParameter("showAll") != null || noFilters) {
            request.setAttribute("payments", all);
        } else {
            List<Payment> filtered = all;
            if (pid != null && !pid.isEmpty()) {
                int id = Integer.parseInt(pid);
                filtered = filtered.stream()
                        .filter(p -> p.getPaymentID() == id)
                        .collect(Collectors.toList());
            }
            if (date != null && !date.isEmpty()) {
                LocalDate d = LocalDate.parse(date);
                filtered = filtered.stream()
                        .filter(p -> p.getPaymentDate()
                                .toInstant()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate()
                                .equals(d))
                        .collect(Collectors.toList());
            }
            request.setAttribute("payments", filtered);
        }

        request.getRequestDispatcher("/paymentList.jsp")
                .forward(request, response);
    }
}