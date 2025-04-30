package model.dao;

import java.sql.Connection;
import java.sql.SQLException;

public class CartItemDAO extends AbstractDAO {
    public CartItemDAO(Connection conn) throws SQLException {
        super(conn);
    }
    // Interaction with the database for cart item-related operations
}
