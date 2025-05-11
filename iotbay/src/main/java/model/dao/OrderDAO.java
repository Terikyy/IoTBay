package model.dao;

import model.Order;

import java.sql.Connection;
import java.sql.PreparedStatement;
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
        String query = "INSERT INTO Order (UserID, AddressID, TrackingNumber, OrderStatus, OrderDate, TotalPrice) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, order.getUserID());
            ps.setInt(2, order.getAddressID());
            ps.setString(3, order.getTrackingNumber());
            ps.setString(4, order.getOrderStatus());
            ps.setDate(5, new java.sql.Date(order.getOrderDate().getTime()));
            ps.setDouble(6, order.getTotalPrice());

            return ps.executeUpdate(); // Returns the number of rows affected
        }
    }

    @Override
    public int update(Order order) throws SQLException {
        String query = "UPDATE Order SET UserID = ?, AddressID = ?, TrackingNumber = ?, OrderStatus = ?, OrderDate = ?, TotalPrice = ? WHERE OrderID = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(2, order.getUserID());
            ps.setInt(3, order.getAddressID());
            ps.setString(4, order.getTrackingNumber());
            ps.setString(5, order.getOrderStatus());
            ps.setDate(6, new java.sql.Date(order.getOrderDate().getTime()));
            ps.setDouble(7, order.getTotalPrice());

            return ps.executeUpdate(); // Returns the number of rows affected
        }
    }

    @Override
    public List<Order> getAll() throws SQLException {
        return queryAllFromTable("Order");
    }

    @Override
    public Order findById(int id) throws SQLException {
        return queryById("Order", "OrderID", id);
    }

    @Override
    public int deleteById(int id) throws SQLException {
        return deleteFromTableById("Order", "OrderID", id);
    }
}
