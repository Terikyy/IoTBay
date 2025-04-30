package model.dao;

import java.sql.*;
import model.Product;

public class ProductDAO extends AbstractDAO<Product> {

    public ProductDAO(Connection conn) throws SQLException {
        super(conn);
    }

    @Override
    protected Product mapRow(ResultSet rs) throws SQLException {
        return new Product(
            rs.getInt("ProductID"),
            rs.getString("Name"),
            rs.getString("Description"),
            rs.getDouble("Price"),
            rs.getInt("Stock"),
            rs.getDate("ReleaseDate")
        );
    }

    @Override
    public int insert(Product product) throws SQLException {
        throw new UnsupportedOperationException("Insert operation is not implemented yet.");
    }

    @Override
    public int update(Product product) throws SQLException {
        throw new UnsupportedOperationException("Update operation is not implemented yet.");
    }
}
