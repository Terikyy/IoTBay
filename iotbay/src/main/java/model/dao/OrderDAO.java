package model.dao;

import model.Order;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class OrderDAO extends AbstractDAO<Order> {

    public OrderDAO(Connection conn) throws SQLException {
        super(conn);
    }

    @Override
    protected Order mapRow(ResultSet rs) throws SQLException {
        return new Order(
                rs.getInt("OrderID"),
                rs.getObject("UserID") != null ? rs.getInt("UserID") : null, // Handle nullable UserID
                rs.getInt("AddressID"),
                rs.getString("TrackingNumber"),
                rs.getString("OrderStatus"),
                rs.getDate("OrderDate"),
                rs.getDouble("TotalPrice")
        );
    }

    @Override
    public int insert(Order order) throws SQLException {
        throw new UnsupportedOperationException("Insert operation is not implemented yet.");
    }

    @Override
    public int update(Order order) throws SQLException {
        throw new UnsupportedOperationException("Update operation is not implemented yet.");
    }

    @Override
    public List<Order> get() throws SQLException {
        throw new UnsupportedOperationException("Get operation is not implemented yet.");
    }

    @Override
    public Order getById(int id) throws SQLException {
        throw new UnsupportedOperationException("Get operation is not implemented yet.");
    }

    @Override
    public int delete(int id) throws SQLException {
        throw new UnsupportedOperationException("Delete operation is not implemented yet.");
    }
}
