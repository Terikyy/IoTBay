package model.dao;

import model.lineproducts.CartItem;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class CartItemDAO extends AbstractDAO<CartItem> {

    public CartItemDAO(Connection conn) throws SQLException {
        super(conn);
    }

    @Override
    protected CartItem mapRow(ResultSet rs) throws SQLException {
        return new CartItem(
                rs.getInt("ProductID"),
                rs.getObject("UserID") != null ? rs.getInt("UserID") : null, // Handle nullable UserID
                rs.getInt("Quantity")
        );
    }

    @Override
    public int insert(CartItem cartItem) throws SQLException {
        throw new UnsupportedOperationException("Insert operation is not implemented yet.");
    }

    @Override
    public int update(CartItem cartItem) throws SQLException {
        throw new UnsupportedOperationException("Update operation is not implemented yet.");
    }

    @Override
    public List<CartItem> getAll() throws SQLException {
        throw new UnsupportedOperationException("Get operation is not implemented yet.");
    }

    @Override
    public CartItem findById(int id) throws SQLException {
        throw new UnsupportedOperationException("Get operation is not implemented yet.");
    }

    @Override
    public int deleteById(int id) throws SQLException {
        throw new UnsupportedOperationException("Delete operation is not implemented yet.");
    }
}
