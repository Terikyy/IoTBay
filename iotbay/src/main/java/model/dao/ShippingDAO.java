package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import model.ShippingManagement;

public class ShippingDAO extends AbstractDAO<ShippingManagement> {

    @Override
    public int insert(ShippingManagement shipment) throws SQLException {
        String query = "INSERT INTO ShippingManagement (OrderID, ShipmentDate, Address, ShippingMethod) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, shipment.getOrderId());
            ps.setString(2, shipment.getShipmentDate().toString());
            ps.setString(3, shipment.getAddress());
            ps.setString(4, shipment.getShippingMethod());
            ps.executeUpdate(); // Execute the insert statement

            ResultSet generatedKeys = ps.getGeneratedKeys();
            if (generatedKeys.next()) {
                return generatedKeys.getInt(1); // Return the generated ShipmentID
            } else {
                throw new SQLException("Creating shipment failed, no ID obtained.");
            }

        }
    }

    public ShippingDAO(Connection conn) throws SQLException {
        super(conn); // Ensure the Connection object is passed to the superclass constructor
    }

    @Override
    protected ShippingManagement mapRow(ResultSet rs) throws SQLException {
        return new ShippingManagement(
                rs.getInt("ShipmentID"),
                rs.getInt("OrderID"),
                LocalDate.parse(rs.getString("ShipmentDate")),
                rs.getString("Address"),
                rs.getString("ShippingMethod"),
                rs.getBoolean("IsFinalised")
        );
    }

    public int create(ShippingManagement shipment) throws SQLException {
        String query = "INSERT INTO ShippingManagement (OrderID, ShipmentDate, Address, ShippingMethod) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, shipment.getOrderId());
            ps.setString(2, shipment.getShipmentDate().toString());
            ps.setString(3, shipment.getAddress());
            ps.setString(4, shipment.getShippingMethod());

            return ps.executeUpdate(); // Returns the number of rows affected
        }
    }

    @Override
    public int update(ShippingManagement shipment) throws SQLException {
        String query = "UPDATE ShippingManagement SET OrderID = ?, ShipmentDate = ?, Address = ?, ShippingMethod = ?, IsFinalised = ? WHERE ShipmentID = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, shipment.getOrderId());
            ps.setString(2, shipment.getShipmentDate().toString());
            ps.setString(3, shipment.getAddress());
            ps.setString(4, shipment.getShippingMethod());
            ps.setBoolean(5, shipment.isFinalised());
            ps.setInt(6, shipment.getShipmentId());

            return ps.executeUpdate(); // Returns the number of rows affected
        }
    }

    //super class method

    @Override
    public int deleteById(int id) throws SQLException {
        String query = "DELETE FROM ShippingManagement WHERE ShipmentID = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, id);
            return ps.executeUpdate(); // Returns the number of rows affected
        }
    }

    @Override
    public List<ShippingManagement> getAll() throws SQLException {
        String query = "SELECT * FROM ShippingManagement";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ResultSet rs = ps.executeQuery();
            List<ShippingManagement> shipments = new ArrayList<>();
            while (rs.next()) {
                shipments.add(mapRow(rs));
            }
            return shipments;
        }
    }

    @Override
    public ShippingManagement findById(int id) throws SQLException {
        String query = "SELECT * FROM ShippingManagement WHERE ShipmentID = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapRow(rs);
            }
            return null; // Return null if no record is found
        }
    }

    public List<ShippingManagement> findByUserId(int userId) throws SQLException {
        String query = "SELECT s.* FROM ShippingManagement s Join `Order` o ON s.OrderID = o.OrderID WHERE o.UserID = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            List<ShippingManagement> shipments = new ArrayList<>();
            while (rs.next()) {
                shipments.add(mapRow(rs));
            }
            return shipments;
        }
    }

    public List<ShippingManagement> findByUserIdAndDate(int userId, LocalDate date) throws SQLException {
        String query = "SELECT s.* FROM ShippingManagement s Join `Order` o ON s.OrderID = o.OrderID WHERE o.UserID = ? AND s.ShipmentDate = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, userId);
            ps.setString(2, date.toString());
            ResultSet rs = ps.executeQuery();
            List<ShippingManagement> shipments = new ArrayList<>();
            while (rs.next()) {
                shipments.add(mapRow(rs));
            }
            return shipments;
        }
    }





}
