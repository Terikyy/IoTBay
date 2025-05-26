package model.dao;

import model.Log;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LogDAO extends AbstractDAO<Log> {
    private final String tableName = "Log";
    private final String tableId = "LogID";

    public LogDAO(Connection conn) throws SQLException {
        super(conn);
    }

    @Override
    protected Log mapRow(ResultSet rs) throws SQLException {
        return new Log(
                rs.getInt(tableId),
                rs.getString("LogMessage"),
                rs.getDate("Timestamp"),
                rs.getInt("userId")
        );
    }

    @Override
    public int insert(Log log) throws SQLException {

        String query = "INSERT INTO Log (LogMessage, LogId, UserId, Timestamp) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, log.getMessage());
            ps.setInt(2, log.getLogId());
            ps.setInt(3, log.getUserId());
            ps.setString(4, new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(log.getTimestamp().getTime())));

            return ps.executeUpdate(); // Returns the number of rows affected
        }
    }

    @Override
    public int update(Log entity) throws SQLException {
        throw new UnsupportedOperationException("Logs should not be tampered with!!");
    }

    @Override
    public List<Log> getAll() throws SQLException {
        return queryAllFromTable(tableName);
    }

    public List<Log> query(String query) throws SQLException {
        List<Log> logs = new ArrayList<>();
        String sqlQuery = "SELECT * FROM Log WHERE LogMessage LIKE ? OR Timestamp LIKE ?";
        try (PreparedStatement ps = conn.prepareStatement(sqlQuery);) {
            ps.setString(1, "%" + query + "%");
            ps.setString(2, "%" + query + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    logs.add(mapRow(rs));
                }
            }
        }
        return logs;
    }

    @Override
    public Log findById(int id) throws SQLException {
        return queryById(tableName, tableId, id);
    }

    @Override
    public int deleteById(int id) throws SQLException {
        throw new UnsupportedOperationException("Logs should not be tampered with!!");
    }
}
