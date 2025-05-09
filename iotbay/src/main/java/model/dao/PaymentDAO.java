package model.dao;

import model.Payment;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class PaymentDAO extends AbstractDAO<Payment> {

    public PaymentDAO(Connection conn) throws SQLException {
        super(conn);
    }

    @Override
    protected Payment mapRow(ResultSet rs) throws SQLException {
        return new Payment(
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
        //add a new payment record to the databse. invole an INSERT sql statement
    }

    @Override
    public int update(Payment payment) throws SQLException {
        throw new UnsupportedOperationException("Update operation is not implemented yet.");
        //updates an existing payment record in the database. invole an UPDATE sql statement using PaymentId as the identifier
    }

    @Override
    public List<Payment> getAll() throws SQLException {
        throw new UnsupportedOperationException("Get operation is not implemented yet.");
        //retrieves all payment records from the databse. would involve a SELECT * FROM Payment sql query
    }

    @Override
    public Payment findById(int id) throws SQLException {
        throw new UnsupportedOperationException("Get operation is not implemented yet.");
        //retrieves a single Payment record by its PaymentID. would invole a SELECT * FROM Payment WHERE PaymentID = ? sql query
    }

    @Override
    public int deleteById(int id) throws SQLException {
        throw new UnsupportedOperationException("Delete operation is not implemented yet.");
        //deletes a payment record by its PaymentID. involves a DELETE FROM Payment WHERE PaymentID = ? sql query
    }
}
