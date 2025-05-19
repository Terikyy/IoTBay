package model.dao;

import model.lineproducts.OrderItem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class OrderItemDAO extends AbstractDAO<OrderItem> {

    public OrderItemDAO(Connection conn) throws SQLException {
        super(conn);
    }

    @Override
    protected OrderItem mapRow(ResultSet rs) throws SQLException {
        return new OrderItem(
                rs.getInt("ProductID"),
                rs.getInt("OrderID"),
                rs.getInt("Quantity"),
                rs.getDouble("PriceOnOrder")
        );
    }

    @Override
    public int insert(OrderItem orderItem) throws SQLException {
        String query = "INSERT INTO OrderItem (ProductID, OrderID, Quantity, PriceOnOrder) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, orderItem.getProductID());
            ps.setInt(2, orderItem.getOrderID());
            ps.setInt(3, orderItem.getQuantity());
            ps.setDouble(5, orderItem.getPriceOnOrder());

            return ps.executeUpdate(); // Returns the number of rows affected
        }
    }

    @Override
    public int update(OrderItem orderItem) throws SQLException {
        String query = "UPDATE OrderItem SET ProductID = ?, OrderID = ?, Quantity = ?, PriceOnOrder = ? WHERE OrderID = ? and ProductID = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, orderItem.getProductID());
            ps.setInt(2, orderItem.getOrderID());
            ps.setInt(3, orderItem.getQuantity());
            ps.setDouble(5, orderItem.getPriceOnOrder());

            return ps.executeUpdate(); // Returns the number of rows affected
        }
    }

    @Override
    public List<OrderItem> getAll() throws SQLException {
        return queryAllFromTable("OrderItem");
    }

    @Override
    public OrderItem findById(int id) throws SQLException {
        return queryById("OrderItem", "OrderID", id);
    }

    @Override
    public int deleteById(int id) throws SQLException {
        return deleteFromTableById("OrderItem", "OrderID", id);
    }

    public int deleteByIds(int orderId, int productId) throws SQLException {
        String query = "DELETE FROM OrderItem WHERE OrderID = ? AND ProductID = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, orderId);
            ps.setInt(2, productId);
            return ps.executeUpdate();
        }
    }
}
