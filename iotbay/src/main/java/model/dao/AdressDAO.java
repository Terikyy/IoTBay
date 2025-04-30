package model.dao;

import java.sql.*;
import model.Address;

public class AdressDAO extends AbstractDAO<Address> {

    public AdressDAO(Connection conn) throws SQLException {
        super(conn);
    }

    @Override
    protected Address mapRow(ResultSet rs) throws SQLException {
        return new Address(
            rs.getInt("AddressID"),
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
}
