package model.dao;

import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

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
