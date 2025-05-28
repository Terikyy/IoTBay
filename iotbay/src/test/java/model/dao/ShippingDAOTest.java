package model.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import model.ShippingManagement;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ShippingDAOTest {

    private static Connection conn;
    private ShippingDAO shippingDAO;

    private final int TEST_ORDER_ID = Integer.MAX_VALUE;
    private final LocalDate TEST_DATE = LocalDate.of(2025, 10, 1);
    private final String TEST_ADDRESS = "123 Test St";
    private final String TEST_METHOD = "Express";
    private final boolean TEST_FINALISED = false;

    private int insertedShipmentId;

    @BeforeAll
    public static void setupClass() throws Exception {
        DBConnector connector = new DBConnector();
        conn = connector.openConnection();
        conn.setAutoCommit(false);
    }

    @BeforeEach
    public void setup() throws SQLException {
        shippingDAO = new ShippingDAO(conn);
    }

    @AfterEach
    public void rollback() throws SQLException {
        conn.rollback();
    }

    @AfterAll
    public static void teardownClass() throws SQLException {
        if (conn != null && !conn.isClosed()) {
            conn.setAutoCommit(true);
            conn.close();
        }
    }

    // US501: Insert new shipment
    @Test
    @Order(1)
    public void testInsert() throws SQLException {
        ShippingManagement shipment = new ShippingManagement(0, TEST_ORDER_ID, TEST_DATE, TEST_ADDRESS, TEST_METHOD, TEST_FINALISED);
        insertedShipmentId = shippingDAO.insert(shipment);
        assertTrue(insertedShipmentId > 0, "Expected generated ShipmentID");

        ShippingManagement found = shippingDAO.findById(insertedShipmentId);
        assertNotNull(found, "Inserted shipment should be found");
        assertEquals(TEST_ORDER_ID, found.getOrderId());
        assertEquals(TEST_METHOD, found.getShippingMethod());
        assertEquals(TEST_ADDRESS, found.getAddress());
        assertEquals(TEST_FINALISED, found.isFinalised());
    }

    // US505: Find shipment by ID
    @Test
    @Order(2)
    public void testFindById() throws SQLException {
        ShippingManagement shipment = new ShippingManagement(0, TEST_ORDER_ID, TEST_DATE, TEST_ADDRESS, TEST_METHOD, TEST_FINALISED);
        int id = shippingDAO.insert(shipment);
        ShippingManagement found = shippingDAO.findById(id);

        assertNotNull(found);
        assertEquals(TEST_ORDER_ID, found.getOrderId());
    }

    // US605: Update shipment
    @Test
    @Order(3)
    public void testUpdate() throws SQLException {
        ShippingManagement shipment = new ShippingManagement(0, TEST_ORDER_ID, TEST_DATE, TEST_ADDRESS, TEST_METHOD, TEST_FINALISED);
        int id = shippingDAO.insert(shipment);

        shipment.setShipmentId(id);
        shipment.setAddress("456 Updated St");
        shipment.setShippingMethod("Standard");
        shipment.setFinalised(true);

        int rowsAffected = shippingDAO.update(shipment);
        assertEquals(1, rowsAffected);

        ShippingManagement updated = shippingDAO.findById(id);
        assertEquals("456 Updated St", updated.getAddress());
        assertEquals("Standard", updated.getShippingMethod());
    }

    // US507: Delete shipment by ID
    @Test
    @Order(4)
    public void testDeleteById() throws SQLException {
        ShippingManagement shipment = new ShippingManagement(0, TEST_ORDER_ID, TEST_DATE, TEST_ADDRESS, TEST_METHOD, TEST_FINALISED);
        int id = shippingDAO.insert(shipment);
        int rowsAffected = shippingDAO.deleteById(id);
        assertEquals(1, rowsAffected);

        ShippingManagement result = shippingDAO.findById(id);
        assertNull(result, "Shipment should be null after deletion");
    }

    // US507:
    @Test
    @Order(5)
    public void testFindByOrderId() throws SQLException {
        ShippingManagement shipment = new ShippingManagement(0, TEST_ORDER_ID, TEST_DATE, TEST_ADDRESS, TEST_METHOD, TEST_FINALISED);
        int id = shippingDAO.insert(shipment);

        List<ShippingManagement> list = shippingDAO.findByOrderId(TEST_ORDER_ID);
        assertNotNull(list);
        assertTrue(list.stream().anyMatch(s -> s.getShipmentId() == id));
    }

    // US505: Get all shipments
    @Test
    @Order(6)
    public void testGetAll() throws SQLException {
        List<ShippingManagement> list = shippingDAO.getAll();
        assertNotNull(list);
        // Just verify list retrieval works — no exact size assertion
    }
}
