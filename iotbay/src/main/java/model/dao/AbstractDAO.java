package model.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractDAO<T> {
    protected Statement st;

    public AbstractDAO(Connection conn) throws SQLException {
        st = conn.createStatement();
    }

    // Abstract method to map a ResultSet row to a specific object
    protected abstract T mapRow(ResultSet rs) throws SQLException;

    // Create (Insert)
    public abstract int insert(T entity) throws SQLException;

    // Update
    public abstract int update(T entity) throws SQLException;

    // Retrieve all records from a table
    public List<T> getAll(String tableName) throws SQLException {
        List<T> results = new ArrayList<>();
        String query = "SELECT * FROM " + tableName;
        try (ResultSet rs = st.executeQuery(query)) {
            while (rs.next()) {
                results.add(mapRow(rs));
            }
        }
        return results;
    }

    // Retrieve records by a specific column value
    public List<T> getByColumn(String tableName, String columnName, Object value) throws SQLException {
        List<T> results = new ArrayList<>();
        String query = "SELECT * FROM " + tableName + " WHERE " + columnName + " = ?";
        try (PreparedStatement ps = st.getConnection().prepareStatement(query)) {
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
    public T getById(String tableName, String idColumn, Object idValue) throws SQLException {
        String query = "SELECT * FROM " + tableName + " WHERE " + idColumn + " = ?";
        try (PreparedStatement ps = st.getConnection().prepareStatement(query)) {
            ps.setObject(1, idValue);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        }
        return null;
    }

    // Delete a record by ID
    public int deleteById(String tableName, String idColumn, Object idValue) throws SQLException {
        String query = "DELETE FROM " + tableName + " WHERE " + idColumn + " = ?";
        try (PreparedStatement ps = st.getConnection().prepareStatement(query)) {
            ps.setObject(1, idValue);
            return ps.executeUpdate();
        }
    }

    // Count the total number of records in a table
    public int count(String tableName) throws SQLException {
        String query = "SELECT COUNT(*) AS total FROM " + tableName;
        try (ResultSet rs = st.executeQuery(query)) {
            if (rs.next()) {
                return rs.getInt("total");
            }
        }
        return 0;
    }

    // Execute a custom query and return a list of mapped objects
    public List<T> executeCustomQuery(String query, Object... params) throws SQLException {
        List<T> results = new ArrayList<>();
        try (PreparedStatement ps = st.getConnection().prepareStatement(query)) {
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    results.add(mapRow(rs));
                }
            }
        }
        return results;
    }
}
