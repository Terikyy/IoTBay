package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Address;

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
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, userId);
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                addresses.add(mapRow(rs));
                }
            }
        }
        return addresses;
    }

    public Address findByIdAndUserId(int addressId, int userId) throws SQLException {
        String sql = "SELECT * FROM Address WHERE AddressID = ? AND UserID = ?";
        try (var preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, addressId);
            preparedStatement.setInt(2, userId);
            try (var resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return mapRow(resultSet);
                }
            }
        }
        return null; // Return null if no matching address is found
    }

    @Override
    public int insert(Address address) throws SQLException {
        String sql = "INSERT INTO addresses (Name, StreetNumber, StreetName, Postcode, Suburb, City, State) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, address.getName());
            stmt.setInt(2, address.getStreetNumber());
            stmt.setString(3, address.getStreetName());
            stmt.setInt(4, address.getPostcode());
            stmt.setString(5, address.getSuburb());
            stmt.setString(6, address.getCity());
            stmt.setString(7, address.getState());
            stmt.executeUpdate();

            //Retreieve the generated AddressID
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1); //return generated AddressID
                }
            }
            throw new SQLException("Failed to insert address and retrieve AddressID.");
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
    public int deleteById(int id) throws SQLException {
        String sql = "DELETE FROM Address WHERE AddressID = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate(); // Returns the number of rows deleted
        }
    }

    @Override
    public int update(Address entity) throws SQLException {
        throw new UnsupportedOperationException("Updating an Address is not supported because Address is immutable.");
    }
}


