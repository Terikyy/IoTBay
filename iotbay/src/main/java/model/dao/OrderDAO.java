package model.dao;

import java.sql.Connection;
import java.sql.SQLException;

public class OrderDAO extends AbstractDAO {
    public OrderDAO(Connection conn) throws SQLException {
        super(conn);
    }
    // Interaction with the database for order-related operations
}
