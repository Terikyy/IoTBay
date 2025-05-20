package controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Payment;
import model.dao.PaymentDAO;
import model.dao.UserDAO;
import model.users.User;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@WebServlet("/PaymentListController")
public class PaymentListController extends HttpServlet {
    private PaymentDAO paymentDAO;
    private UserDAO userDAO;


    // doGet Method
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        paymentDAO = (PaymentDAO) session.getAttribute("paymentDAO");
        userDAO = (UserDAO) session.getAttribute("userDAO");


        try {
            if (paymentDAO == null) {
                String currentURL = request.getRequestURI();
                if (request.getQueryString() != null) {
                    currentURL += "?" + request.getQueryString();
                }
                response.sendRedirect(request.getContextPath() + "/Connservlet?redirectURL=" + currentURL);
                return;
            }

            // Check if the user is logged in
            // Showing payments created by customer user
            User user = (User) session.getAttribute("user");
            List<Payment> all;
            if (user != null) { // logged in user
                int userID = user.getId();
                all = paymentDAO.findByUserId(userID);
            } else { // guest user
                all = new ArrayList<>();
            }

            // otherwise filter by paymentId and/or paymentDate
            String sid = request.getParameter("paymentId");
            String date = request.getParameter("paymentDate");

            boolean noFilters = (sid == null || sid.isEmpty())
                    && (date == null || date.isEmpty());
            if (request.getParameter("showAll") != null || noFilters) {
                request.setAttribute("payments", all);
                request.getRequestDispatcher("/paymentList.jsp")
                        .forward(request, response);
                return;
            }

            List<Payment> filtered = all;
            // Filter by paymentId and/or paymentDate
            if (!sid.isEmpty() && !date.isEmpty()) {
                int id = Integer.parseInt(sid);
                LocalDate d = LocalDate.parse(date);
                filtered = all.stream()

                        .filter(s -> s.getPaymentID() == id
                                && s.getPaymentDate().equals(d))
                        .collect(Collectors.toList());
            } else if (!sid.isEmpty()) { //filter by paymentId only
                int id = Integer.parseInt(sid);
                filtered = filtered.stream()
                        .filter(s -> s.getPaymentID() == id)
                        .collect(Collectors.toList());
            } else if (date != null && !date.isEmpty()) { //filter by paymentDate only
                LocalDate d = LocalDate.parse(date);
                filtered = filtered.stream()
                        .filter(s -> s.getPaymentDate().equals(d))
                        .collect(Collectors.toList());
            }

            request.setAttribute("payments", filtered);

            request.getRequestDispatcher("/paymentList.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Error handling payment action", e);
        }
    }
}

