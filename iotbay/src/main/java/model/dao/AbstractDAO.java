package model.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class AbstractDAO {
    Statement st;

    public AbstractDAO(Connection conn) throws SQLException {
        st = conn.createStatement();
    }
}
