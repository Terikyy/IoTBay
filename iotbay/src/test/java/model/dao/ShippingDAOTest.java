package model.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.Test;

import model.ShippingManagement;

public class ShippingDAOTest {

    ShippingDAO shippingDAO;
    ShippingManagement shipping = new ShippingManagement(
            Integer.MAX_VALUE,
            54321,
            LocalDate.now(),
            "B11 UTS",
            "Express",
            false
    );

    public ShippingDAOTest() throws SQLException, ClassNotFoundException {
        DBConnector db = new DBConnector();
        try {
            shippingDAO = new ShippingDAO(db.openConnection());
        } catch (Exception e) {
            fail("Failed to initialize ShippingDAO: " + e.getMessage());
        }
    }

    @Test
    public void testInsert() throws SQLException {
     
        if (shippingDAO.findById(shipping.getShipmentId()) != null) cleanShipping();

        try {
            shippingDAO.insert(shipping);
        } catch (Exception e) {
            fail("Insert failed: " + e.getMessage());
        }
        ShippingManagement result = shippingDAO.findById(shipping.getShipmentId());
        if (result == null) {
            fail("ShippingManagement not found after insert");
        }
        
        assertEquals(shipping.getShipmentId(), result.getShipmentId());
        assertEquals(shipping.getOrderId(), result.getOrderId());
        assertEquals(shipping.getShippingMethod(), result.getShippingMethod());
        assertEquals(shipping.getShipmentDate(), result.getShipmentDate());
        assertEquals(shipping.getAddress(), result.getAddress());
        assertEquals(shipping.isFinalised(), result.isFinalised());

        cleanShipping();
    }

    public void cleanShipping() throws SQLException {
        String query = "DELETE FROM ShippingManagement WHERE ShipmentID = ?";
        try (PreparedStatement ps = shippingDAO.conn.prepareStatement(query)) {
            ps.setInt(1, shipping.getShipmentId());
            ps.executeUpdate(); 
        }
    }

    @Test
    public void testUpdate() throws SQLException{
        testInsert();

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

        testInsert();

        int deletedRows = shippingDAO.deleteById(shipping.getShipmentId());
        assertEquals(1, deletedRows, "Expected 1 row to be deleted");

        ShippingManagement result = shippingDAO.findById(shipping.getShipmentId());
        assertNull(result, "Expected shipment to be null after deletion");
    }

    
}
