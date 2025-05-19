package model.dao;

import model.Log;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class LogDAO extends AbstractDAO<Log> {

    public LogDAO(Connection conn) throws SQLException {
        super(conn);
    }

    @Override
    protected Log mapRow(ResultSet rs) throws SQLException {
        return new Log(
                rs.getInt("LogID"),
                rs.getString("LogMessage"),
                rs.getDate("Timestamp")
        );

    }

    @Override
    public int insert(Log log) throws SQLException {
        String query = "INSERT INTO Log (LogID, LogMessage, Timestamp) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, log.getLogId());
            ps.setString(2, log.getMessage());
            ps.setDate(3, new java.sql.Date(log.getTimestamp().getTime()));

            return ps.executeUpdate(); // Returns the number of rows affected
        }
    }

    @Override
    public int update(Log entity) throws SQLException {
        throw new UnsupportedOperationException("Logs should not be tampered with!!");
    }

    @Override
    public List<Log> getAll() throws SQLException {
        return queryAllFromTable("Log");
    }

    @Override
    public Log findById(int id) throws SQLException {
        return queryById("Log", "LogID", id);
    }

    @Override
    public int deleteById(int id) throws SQLException {
        String query = "DELETE FROM Log WHERE LogID = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, id);
            return ps.executeUpdate(); // Returns the number of rows affected
        }
    }
}
