package model.dao;

import model.Product;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

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

    @Override
    public List<Product> get() throws SQLException {
        throw new UnsupportedOperationException("Get operation is not implemented yet.");
    }

    @Override
    public Product getById(int id) throws SQLException {
        throw new UnsupportedOperationException("Get operation is not implemented yet.");
    }

    @Override
    public int delete(int id) throws SQLException {
        throw new UnsupportedOperationException("Delete operation is not implemented yet.");
    }
}
