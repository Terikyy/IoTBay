package model.dao;

import model.Payment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PaymentDAO extends AbstractDAO<Payment> {

    public PaymentDAO(Connection conn) throws SQLException {
        super(conn);
    }

    @Override
    protected Payment mapRow(ResultSet rs) throws SQLException {
        return new Payment(
                rs.getInt("PaymentID"),
                rs.getInt("OrderID"),
                rs.getString("PaymentMethod"),
                rs.getDouble("AmountPaid"),
                rs.getDate("PaymentDate"),
                rs.getString("PaymentStatus"));
    }

    @Override
    public int insert(Payment payment) throws SQLException { // add a new payment record to the databse. invole an
        // INSERT sql statement
        String query = "INSERT INTO Payment (PaymentID, OrderID, PaymentMethod, AmountPaid, PaymentDate, PaymentStatus) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, payment.getPaymentID());
            ps.setInt(2, payment.getOrderID());
            ps.setString(3, payment.getPaymentMethod());
            ps.setDouble(4, payment.getAmountPaid());
            ps.setDate(5, new java.sql.Date(payment.getPaymentDate().getTime()));
            ps.setString(6, payment.getPaymentStatus());

            return ps.executeUpdate(); // Returns the number of rows affected
        }
    }

    @Override
    public int update(Payment payment) throws SQLException { // updates an existing payment record in the database.
        // invole an UPDATE sql statement using PaymentId as the
        // identifier
        String query = "UPDATE Payment SET OrderID = ?, PaymentMethod = ?, AmountPaid = ?, PaymentDate = ?, PaymentStatus = ? WHERE PaymentID = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, payment.getOrderID());
            ps.setString(2, payment.getPaymentMethod());
            ps.setDouble(3, payment.getAmountPaid());
            ps.setDate(4, new java.sql.Date(payment.getPaymentDate().getTime()));
            ps.setString(5, payment.getPaymentStatus());
            ps.setInt(6, payment.getPaymentID());

            return ps.executeUpdate(); // Returns the number of rows affected
        }
    }

    @Override
    public List<Payment> getAll() throws SQLException { // retrieves all payment records from the databse. would involve
        // a SELECT * FROM Payment sql query
        return queryAllFromTable("Payment");
    }

    @Override
    public Payment findById(int id) throws SQLException { // retrieves a single Payment record by its PaymentID. would
        // invole a SELECT * FROM Payment WHERE PaymentID = ? sql
        // query
        return queryById("Payment", "PaymentID", id);
    }

    public List<Payment> findByUserId(int userId) throws SQLException {
        String query = "SELECT s.* FROM Payment s Join `Order` o ON s.OrderID = o.OrderID WHERE o.UserID = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            List<Payment> payments = new ArrayList<>();
            while (rs.next()) {
                payments.add(mapRow(rs));
            }
            return payments;
        }
    }

    @Override
    public int deleteById(int id) throws SQLException { // deletes a payment record by its PaymentID. involves a DELETE
        // FROM Payment WHERE PaymentID = ? sql query
        return deleteFromTableById("Payment", "PaymentID", id);
    }
}
