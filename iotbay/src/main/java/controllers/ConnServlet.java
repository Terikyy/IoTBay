package controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.dao.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/Connservlet")
public class ConnServlet extends HttpServlet {

    private DBConnector db;

    private AddressDAO addressDAO;
    private CartItemDAO cartItemDAO;
    private OrderDAO orderDAO;
    private OrderItemDAO orderItemDAO;
    private PaymentDAO paymentDAO;
    private ProductDAO productDAO;
    private UserDAO userDAO;
    private AdminDAO adminDAO;
    private StaffDAO staffDAO;
    private ShippingDAO shippingDAO;

    private Connection conn;

    @Override // Create and instance of DBConnector for the deployment session
    public void init() {

        try {

            db = new DBConnector();

        } catch (ClassNotFoundException | SQLException ex) {

            Logger.getLogger(ConnServlet.class.getName()).log(Level.SEVERE, null, ex);

        }

    }

    public static void updateDAOsGET(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String currentURL = request.getRequestURI();
        if (request.getQueryString() != null) {
            currentURL += "?" + request.getQueryString();
        }
        String encodedURL = URLEncoder.encode(currentURL, "UTF-8");
        response.sendRedirect(request.getContextPath() + "/Connservlet?redirectURL=" + encodedURL);
    }

    public static void updateDAOsPOST(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String currentURL = request.getRequestURI();
        if (request.getQueryString() != null) {
            currentURL += "?" + request.getQueryString();
        }
        String encodedURL = URLEncoder.encode(currentURL, "UTF-8");
        response.sendRedirect(request.getContextPath() + "/Connservlet?redirectURL=" + encodedURL + "&method=POST");
    }

    @Override // Add the DBConnector, DBManager, Connection instances to the session
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        HttpSession session = request.getSession();

        conn = db.openConnection();

        try {
            // TODO: Add constructors for the daos (managers)
            addressDAO = new AddressDAO(conn);
            cartItemDAO = new CartItemDAO(conn);
            orderDAO = new OrderDAO(conn);
            orderItemDAO = new OrderItemDAO(conn);
            paymentDAO = new PaymentDAO(conn);
            productDAO = new ProductDAO(conn);
            userDAO = new UserDAO(conn);
            adminDAO = new AdminDAO(conn);
            staffDAO = new StaffDAO(conn);
            shippingDAO = new ShippingDAO(conn);

        } catch (SQLException ex) {
            Logger.getLogger(ConnServlet.class.getName()).log(Level.SEVERE, null, ex);

        }

        // export the DB manager to the view-session (JSPs)
        session.setAttribute("addressDAO", addressDAO);
        session.setAttribute("cartItemDAO", cartItemDAO);
        session.setAttribute("orderDAO", orderDAO);
        session.setAttribute("orderItemDAO", orderItemDAO);
        session.setAttribute("paymentDAO", paymentDAO);
        session.setAttribute("productDAO", productDAO);
        session.setAttribute("userDAO", userDAO);
        session.setAttribute("adminDAO", adminDAO);
        session.setAttribute("staffDAO", staffDAO);
        session.setAttribute("shippingDAO", shippingDAO);

        // Get the original requested URL from the referer or a parameter
        String redirectURL = request.getParameter("redirectURL");
        if (redirectURL != null && !redirectURL.isEmpty()) {
            response.sendRedirect(redirectURL);
        } else {
            // Default redirect to products list if no specific URL was requested
            response.sendRedirect(request.getContextPath() + "/products/list");
        }

    }

    @Override // Destroy the servlet and release the resources of the application (terminate
    // also the db connection)
    public void destroy() {

        try {

            db.closeConnection();

        } catch (SQLException ex) {

            Logger.getLogger(ConnServlet.class.getName()).log(Level.SEVERE, null, ex);

        }

    }

}