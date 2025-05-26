package model.dao;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class StaffDAOTest {
    StaffDAO staffDAO;

    public StaffDAOTest() throws SQLException, ClassNotFoundException {
        DBConnector db = new DBConnector();
        try {
            staffDAO = new StaffDAO(db.openConnection());
        } catch (Exception e) {
            fail("Failed to initialize StaffDAO: " + e.getMessage());
        }
    }

    @Test
    @Order(0)
    public void testIsStaff() throws SQLException {
        boolean result = staffDAO.isStaff(1);
        assertTrue(result);
    }

    @Test
    @Order(1)
    public void testInsert() throws SQLException {
        try {
            staffDAO.insert(Integer.MAX_VALUE);
        } catch (Exception e) {
            fail("Insert failed: " + e.getMessage());
        }
        boolean result = staffDAO.isStaff(Integer.MAX_VALUE);
        assertTrue(result);
    }

    @Test
    @Order(2)
    public void testDelete() throws SQLException {
        try {
            staffDAO.delete(Integer.MAX_VALUE);
        } catch (Exception e) {
            fail("Delete failed: " + e.getMessage());
        }
        boolean result = staffDAO.isStaff(Integer.MAX_VALUE);
        assertFalse(result);
    }
}
