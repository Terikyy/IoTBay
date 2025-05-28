package model.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.Test;

import model.ShippingManagement;

public class ShippingDAOTest {

    ShippingDAO shippingDAO;
    ShippingManagement shipping;

    public ShippingDAOTest() throws SQLException, ClassNotFoundException {
        DBConnector db = new DBConnector();
        try {
            shippingDAO = new ShippingDAO(db.openConnection());
        } catch (Exception e) {
            fail("Failed to initialize ShippingDAO: " + e.getMessage());
        }
    }

    private void insertShipping() throws SQLException {
        shipping = new ShippingManagement(
                0,
                54321,
                LocalDate.now(),
                "B11 UTS",
                "Express",
                false
        );
    
        String sql = "INSERT INTO ShippingManagement (OrderID, ShipmentDate, Address, ShippingMethod, IsFinalised) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = shippingDAO.conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, shipping.getOrderId());
            ps.setObject(2, shipping.getShipmentDate());
            ps.setString(3, shipping.getAddress());
            ps.setString(4, shipping.getShippingMethod());
            ps.setBoolean(5, shipping.isFinalised());
            ps.executeUpdate();
    
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    shipping.setShipmentId(rs.getInt(1));
                }
            }
        }
    }

    private void cleanShipping() throws SQLException {
        String query = "DELETE FROM ShippingManagement WHERE ShipmentID = ?";
        try (PreparedStatement ps = shippingDAO.conn.prepareStatement(query)) {
            ps.setInt(1, shipping.getShipmentId());
            ps.executeUpdate();
        }
    }

    @Test
    public void testInsert() throws SQLException {
        insertShipping();

        ShippingManagement result = shippingDAO.findById(shipping.getShipmentId());
        assertNotNull(result, "ShippingManagement not found after insert");

        assertEquals(shipping.getOrderId(), result.getOrderId());
        assertEquals(shipping.getShippingMethod(), result.getShippingMethod());
        assertEquals(shipping.getShipmentDate(), result.getShipmentDate());
        assertEquals(shipping.getAddress(), result.getAddress());
        assertEquals(shipping.isFinalised(), result.isFinalised());

        cleanShipping();
    }

    @Test
    public void testUpdate() throws SQLException {
        insertShipping();

        shipping.setShippingMethod("Standard");
        shipping.setAddress("B12 UTS");

        int updatedRows = shippingDAO.update(shipping);
        assertEquals(1, updatedRows, "Expected 1 row to be updated");

        ShippingManagement result = shippingDAO.findById(shipping.getShipmentId());
        assertNotNull(result, "Expected shipment to not be null after update");
        assertEquals("Standard", result.getShippingMethod(), "Expected shipping method to be updated");
        assertEquals("B12 UTS", result.getAddress(), "Expected address to be updated");

        cleanShipping();
    }

    @Test
    public void testDelete() throws SQLException {
        insertShipping();

        int deletedRows = shippingDAO.deleteById(shipping.getShipmentId());
        assertEquals(1, deletedRows, "Expected 1 row to be deleted");

        ShippingManagement result = shippingDAO.findById(shipping.getShipmentId());
        assertNull(result, "Expected shipment to be null after deletion");
    }
}
