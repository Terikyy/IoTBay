package model.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import model.ShippingManagement;

public class ShipmentDAO extends AbstractDAO<ShippingManagement> {

    public ShipmentDAO(Connection conn) throws SQLException {
        super(conn);
    }

    @Override
    protected ShippingManagement mapRow(ResultSet rs) throws SQLException {
        return new ShippingManagement(
            rs.getInt("shipment_id"),
            rs.getInt("order_id"),
            rs.getDate("shipment_date").toLocalDate(),
            rs.getString("address"),
            rs.getString("delivery_method")
        );
    }

    @Override
    public int insert(ShippingManagement shipment) throws SQLException {
        String sql = """
            INSERT INTO shipments
              (order_id, shipment_date, address, delivery_method)
            VALUES (?, ?, ?, ?)""";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, shipment.getOrderId());
            ps.setDate(2, Date.valueOf(shipment.getShipmentDate()));
            ps.setString(3, shipment.getAddress());
            ps.setString(4, shipment.getDeliveryMethod());
            return ps.executeUpdate();
        }
    }

    @Override
    public int update(ShippingManagement shipment) throws SQLException {
        String sql = """
            UPDATE shipments
            SET order_id=?, shipment_date=?, address=?, delivery_method=?
            WHERE shipment_id=?""";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, shipment.getOrderId());
            ps.setDate(2, Date.valueOf(shipment.getShipmentDate()));
            ps.setString(3, shipment.getAddress());
            ps.setString(4, shipment.getDeliveryMethod());
            ps.setInt(5, shipment.getShipmentId());
            return ps.executeUpdate();
        }
    }

    @Override
    public int deleteById(int shipmentId) throws SQLException {
        String sql = "DELETE FROM shipments WHERE shipment_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, shipmentId);
            return ps.executeUpdate();
        }
    }

    @Override
    public List<ShippingManagement> getAll() throws SQLException {
        return queryAllFromTable("shipments");
    }

    @Override
    public ShippingManagement findById(int id) throws SQLException {
        String sql = "SELECT * FROM shipments WHERE shipment_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        }
        return null;
    }

    // NEW: find all shipments on a given date
    public List<ShippingManagement> findByDate(LocalDate date) throws SQLException {
        String sql = "SELECT * FROM shipments WHERE shipment_date = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(date));
            try (ResultSet rs = ps.executeQuery()) {
                List<ShippingManagement> list = new ArrayList<>();
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
                return list;
            }
        }
    }

}