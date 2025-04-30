package model.dao;

import java.sql.*;
import model.lineproducts.CartItem;

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
}
