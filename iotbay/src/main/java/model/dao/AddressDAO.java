package model.dao;

import model.Address;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class AddressDAO extends AbstractDAO<Address> {

    public AddressDAO(Connection conn) throws SQLException {
        super(conn);
    }

    @Override
    protected Address mapRow(ResultSet rs) throws SQLException {
        return new Address(
                rs.getString("Name"),
                rs.getInt("StreetNumber"),
                rs.getString("StreetName"),
                rs.getInt("Postcode"),
                rs.getString("Suburb"),
                rs.getString("City"),
                rs.getString("State")
        );
    }

    @Override
    public int insert(Address address) throws SQLException {
        throw new UnsupportedOperationException("Insert operation is not implemented yet.");
    }

    @Override
    public int update(Address address) throws SQLException {
        throw new UnsupportedOperationException("Update operation is not implemented yet.");
    }

    @Override
    public List<Address> getAll() throws SQLException {
        throw new UnsupportedOperationException("Get operation is not implemented yet.");
    }

    @Override
    public Address findById(int id) throws SQLException {
        throw new UnsupportedOperationException("Get operation is not implemented yet.");
    }

    @Override
    public int deleteById(int id) throws SQLException {
        throw new UnsupportedOperationException("Delete operation is not implemented yet.");
    }
}
