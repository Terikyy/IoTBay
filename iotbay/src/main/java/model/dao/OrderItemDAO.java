package model.dao;

import model.lineproducts.OrderItem;

import java.sql.Connection;
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
        throw new UnsupportedOperationException("Insert operation is not implemented yet.");
    }

    @Override
    public int update(OrderItem orderItem) throws SQLException {
        throw new UnsupportedOperationException("Update operation is not implemented yet.");
    }

    @Override
    public List<OrderItem> getAll() throws SQLException {
        throw new UnsupportedOperationException("Get operation is not implemented yet.");
    }

    @Override
    public OrderItem findById(int id) throws SQLException {
        throw new UnsupportedOperationException("Get operation is not implemented yet.");
    }

    @Override
    public int deleteById(int id) throws SQLException {
        throw new UnsupportedOperationException("Delete operation is not implemented yet.");
    }
}
