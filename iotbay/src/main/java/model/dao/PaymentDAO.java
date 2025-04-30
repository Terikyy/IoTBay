package model.dao;

import java.sql.Connection;
import java.sql.SQLException;

public class PaymentDAO extends AbstractDAO {
    public PaymentDAO(Connection conn) throws SQLException {
        super(conn);
    }
    // Interactiont with the database for payment-related operations
}
