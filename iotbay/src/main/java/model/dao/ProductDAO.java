package model.dao;

import java.sql.Connection;
import java.sql.SQLException;

public class ProductDAO extends AbstractDAO {
    public ProductDAO(Connection conn) throws SQLException {
        super(conn);
    }
    // Interaction with the database for product-related operations
}
