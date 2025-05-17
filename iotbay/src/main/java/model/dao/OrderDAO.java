package model.dao;

import model.Order;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class OrderDAO extends AbstractDAO<Order> {
    public static final String TABLE_NAME = "`Order`";
    public static final String ID_COLUMN_NAME = "OrderID";
    public static final String USER_ID_COLUMN_NAME = "UserID";

    public OrderDAO(Connection conn) throws SQLException {
        super(conn);
    }

    @Override
    protected Order mapRow(ResultSet rs) throws SQLException {
        return new Order(
                rs.getObject("UserID") != null ? rs.getInt("UserID") : null,
                rs.getInt("ShipmentId"),
                rs.getString("OrderStatus"),
                rs.getDate("OrderDate"),
                rs.getDouble("TotalPrice")
        );
    }

    @Override
    public int insert(Order order) throws SQLException {
        String query = "INSERT INTO `Order` (OrderID, UserID, ShipmentId, OrderStatus, OrderDate, TotalPrice) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, order.getOrderID());
            ps.setInt(2, order.getUserID());
            ps.setNull(3, java.sql.Types.INTEGER);
            ps.setString(4, order.getOrderStatus());
            ps.setDate(5, new java.sql.Date(order.getOrderDate().getTime()));
            ps.setDouble(6, order.getTotalPrice());

            return ps.executeUpdate(); // Returns the number of rows affected
        }
    }

    @Override
    public int update(Order order) throws SQLException {
        String query = "UPDATE `Order` SET UserID = ?, ShipmentId = ?, OrderStatus = ?, OrderDate = ?, TotalPrice = ? WHERE OrderID = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, order.getUserID());
            ps.setInt(2, order.getShipmentId());
            ps.setString(3, order.getOrderStatus());
            ps.setDate(4, new java.sql.Date(order.getOrderDate().getTime()));
            ps.setDouble(5, order.getTotalPrice());
            ps.setInt(6, order.getOrderID());

            return ps.executeUpdate(); // Returns the number of rows affected
        }
    }

    public int updateStatus(int orderId, String status) throws SQLException {
        String query = "UPDATE `Order` SET OrderStatus = ? WHERE OrderID = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, status);
            ps.setInt(2, orderId);
            return ps.executeUpdate(); // Returns the number of rows affected
        }
    }

    @Override
    public List<Order> getAll() throws SQLException {
        return queryAllFromTable(TABLE_NAME);
    }

    @Override
    public Order findById(int id) throws SQLException {
        return queryById(TABLE_NAME, "OrderID", id);
    }

    public List<Order> findByUserId(int userId) throws SQLException {
        return queryByColumnValue(TABLE_NAME, "UserID", userId);
    }

    @Override
    public int deleteById(int id) throws SQLException {
        return deleteFromTableById(TABLE_NAME, "OrderID", id);
    }
}
