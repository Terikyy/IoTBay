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
    public abstract List<T> getAll() throws SQLException;

    // Retrieve by ID
    public abstract T findById(int id) throws SQLException;

    // Delete
    public abstract int deleteById(int id) throws SQLException;


    //Helper Functions:

    // Retrieve all records from a table
    protected List<T> queryAllFromTable(String tableName) throws SQLException {
        List<T> results = new ArrayList<>();
        String query = "SELECT * FROM " + tableName;
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    results.add(mapRow(rs));
                }
            }
        }
        return results;
    }

    // Retrieve records by a specific column value -- I am pretty sure this is not best practice
    protected List<T> queryByColumnValue(String tableName, String columnName, Object value) throws SQLException {
        List<T> results = new ArrayList<>();
        String query = "SELECT * FROM " + tableName + " WHERE " + columnName + " = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setObject(1, value);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    results.add(mapRow(rs));
                }
            }
        }
        return results;
    }

    // Retrieve a single record by ID (assumes primary key is a single column)
    protected T queryById(String tableName, String idColumn, int id) throws SQLException {
        return queryByColumnValue(tableName, idColumn, id).stream().findFirst().orElse(null);
    }

    // Delete a record by ID
    protected int deleteFromTableById(String tableName, String idColumn, int id) throws SQLException {
        String query = "DELETE FROM " + tableName + " WHERE " + idColumn + " = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setObject(1, id);
            int result = ps.executeUpdate();
            if (result > 0) {
                return result; // Return the number of rows deleted
            } else {
                throw new SQLException("No record found with the given ID.");
            }
        }
    }
}
