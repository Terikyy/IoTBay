package model.dao;

import model.Address;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AddressDAO extends AbstractDAO<Address> {

    public AddressDAO(Connection conn) throws SQLException {
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

    public List<Address> getByUserId(int userId) throws SQLException {
        List<Address> addresses = new ArrayList<>();
        String sql = "SELECT * FROM Address WHERE UserID = ?";
        try (var preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, userId);
            try (var resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    addresses.add(mapRow(resultSet));
                }
            }
        }
        return addresses;
    }

    @Override
    public int insert(Address address) throws SQLException {
        String sql = "INSERT INTO Address (Name, StreetNumber, StreetName, Postcode, Suburb, City, State) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (var preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, address.getName());
            preparedStatement.setInt(2, address.getStreetNumber());
            preparedStatement.setString(3, address.getStreetName());
            preparedStatement.setInt(4, address.getPostcode());
            preparedStatement.setString(5, address.getSuburb());
            preparedStatement.setString(6, address.getCity());
            preparedStatement.setString(7, address.getState());
            return preparedStatement.executeUpdate();
        }
    }

    @Override
    public int update(Address address) throws SQLException {
        String sql = "UPDATE Address SET Name = ?, StreetNumber = ?, StreetName = ?, Postcode = ?, Suburb = ?, City = ?, State = ? WHERE addressId = ?";
        try (var preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, address.getName());
            preparedStatement.setInt(2, address.getStreetNumber());
            preparedStatement.setString(3, address.getStreetName());
            preparedStatement.setInt(4, address.getPostcode());
            preparedStatement.setString(5, address.getSuburb());
            preparedStatement.setString(6, address.getCity());
            preparedStatement.setString(7, address.getState());
            preparedStatement.setInt(8, address.getAddressID());
            return preparedStatement.executeUpdate();
        }
    }

    @Override
    public List<Address> getAll() throws SQLException {
           String sql = "SELECT * FROM Address";
        List<Address> addresses = new ArrayList<>();
        try (var preparedStatement = conn.prepareStatement(sql);
             var resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                addresses.add(mapRow(resultSet));
            }
            return addresses;
        }
    }

    @Override
    public Address findById(int addressId) throws SQLException {
       String sql = "SELECT * FROM Address WHERE addressId = ?";
        try (var preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, addressId);
            try (var resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return mapRow(resultSet);
                }
            }
        }
        return null; // or throw an exception if not found
    }

    @Override
    public int deleteById(int addressId) throws SQLException {
        String sql = "DELETE FROM Address WHERE addressId = ?";
        try (var preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, addressId);
            return preparedStatement.executeUpdate();
        }
    }
}
