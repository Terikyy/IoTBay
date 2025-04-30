package model.dao;

import java.sql.Connection;
import java.sql.SQLException;

public class UserDAO extends AbstractDAO {
    public UserDAO(Connection conn) throws SQLException {
        super(conn);
    }
    // Interaction with the database for user-related operations
}
