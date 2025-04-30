package model.dao;

import java.sql.*;
import model.lineproducts.OrderItem;

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
        throw new UnsupportedOperationException("Insert operation is not implemented yet.");
    }

    @Override
    public int update(OrderItem orderItem) throws SQLException {
        throw new UnsupportedOperationException("Update operation is not implemented yet.");
    }
}