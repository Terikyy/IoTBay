package model.dao;

import java.sql.*;
import model.Payment;

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
            rs.getString("PaymentStatus")
        );
    }

    @Override
    public int insert(Payment payment) throws SQLException {
        throw new UnsupportedOperationException("Insert operation is not implemented yet.");
    }

    @Override
    public int update(Payment payment) throws SQLException {
        throw new UnsupportedOperationException("Update operation is not implemented yet.");
    }
}
