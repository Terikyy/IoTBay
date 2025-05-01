package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractDAO<T> {
    protected Connection conn;

    public AbstractDAO(Connection conn) throws SQLException {
        this.conn = conn;
    }

    // Abstract method to map a ResultSet row to a specific object
    protected abstract T mapRow(ResultSet rs) throws SQLException;

    // Create (Insert)
    public abstract int insert(T entity) throws SQLException;

    // Update
    public abstract int update(T entity) throws SQLException;

    // Get all records
    public abstract List<T> get() throws SQLException;

    // Retrieve by ID
    public abstract T getById(int id) throws SQLException;

    // Delete
    public abstract int delete(int id) throws SQLException;


    //Helper Functions:

    // Retrieve all records from a table
    protected List<T> getFromTable(String tableName) throws SQLException {
        List<T> results = new ArrayList<>();
        String query = "SELECT * FROM ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, tableName);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    results.add(mapRow(rs));
                }
            }
        }
        return results;
    }

    // Retrieve records by a specific column value -- I am pretty sure this is not best practice
    protected List<T> getFromTableByColumn(String tableName, String columnName, Object value) throws SQLException {
        List<T> results = new ArrayList<>();
        String query = "SELECT * FROM ? WHERE ? = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, tableName);
            ps.setString(2, columnName);
            ps.setObject(3, value);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    results.add(mapRow(rs));
                }
            }
        }
        return results;
    }

    // Retrieve a single record by ID (assumes primary key is a single column)
    protected T getFromTableById(String tableName, String idColumn, int id) throws SQLException {
        return getFromTableByColumn(tableName, idColumn, id).stream().findFirst().orElse(null);
    }

    // Delete a record by ID
    protected int deleteFromTable(String tableName, String idColumn, int id) throws SQLException {
        String query = "DELETE FROM ? WHERE ? = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, tableName);
            ps.setString(2, idColumn);
            ps.setObject(3, id);
            return ps.executeUpdate();
        }
    }
}
