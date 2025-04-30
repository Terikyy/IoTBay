package model.dao;

import java.sql.Connection;
import java.sql.SQLException;

public class AdressDAO extends AbstractDAO {
    public AdressDAO(Connection conn) throws SQLException {
        super(conn);
    }
    // Interaction with the database for address-related operations
}
